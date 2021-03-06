package com.kangyonggan.gitlab.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.kangyonggan.gitlab.annotation.MethodLog;
import com.kangyonggan.gitlab.constants.AppConstants;
import com.kangyonggan.gitlab.constants.EmailTemplateCode;
import com.kangyonggan.gitlab.constants.YesNo;
import com.kangyonggan.gitlab.dto.UserRequest;
import com.kangyonggan.gitlab.mapper.UserMapper;
import com.kangyonggan.gitlab.model.Email;
import com.kangyonggan.gitlab.model.EmailTemplate;
import com.kangyonggan.gitlab.model.User;
import com.kangyonggan.gitlab.service.*;
import com.kangyonggan.gitlab.util.Digests;
import com.kangyonggan.gitlab.util.Encodes;
import com.kangyonggan.gitlab.util.ShellUtil;
import com.kangyonggan.gitlab.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.UUID;

/**
 * @author kyg
 */
@Service
public class UserServiceImpl extends BaseService<User> implements UserService {

    @Autowired
    private EmailTemplateService emailTemplateService;

    @Autowired
    private EmailService emailService;

    @Value("${app.email.username}")
    private String username;

    @Value("${app.email.expires-in}")
    private String expiresIn;

    @Value("${app.name}")
    private String appName;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupUserService groupUserService;

    @Value("${gitlab.htpasswd-path}")
    private String htpasswdPath;

    @Autowired
    private ProjectService projectService;

    @Override
    public boolean existsUsername(String username) {
        User user = new User();
        user.setUsername(username);
        return baseMapper.selectCount(user) > 0;
    }

    @Override
    public boolean existsEmail(String email) {
        User user = new User();
        user.setEmail(email);
        return baseMapper.selectCount(user) > 0;
    }

    @Override
    @MethodLog
    @Transactional(rollbackFor = Exception.class)
    public void saveUser(User user) throws Exception {
        String pwd = user.getPassword();
        entryptPassword(user);

        user.setLastSignInIp(null);
        user.setLastSignInTime(null);
        user.setIsDeleted(YesNo.NO.getCode());
        user.setCreatedTime(null);
        user.setUpdatedTime(null);

        baseMapper.insertSelective(user);

        // 保存htpasswd
        ShellUtil.exec("htpasswd -b", htpasswdPath + "/gitlab.htpasswd", user.getUsername(), pwd);
    }

    @Override
    @MethodLog
    public User findUserByUsernameOrEmail(String username) {
        User user = new User();
        if (username.contains("@")) {
            user.setEmail(username);
            return baseMapper.selectOne(user);
        }

        user.setUsername(username);
        return baseMapper.selectOne(user);
    }

    @Override
    @MethodLog
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(User user) throws Exception {
        User oldUser = baseMapper.selectByPrimaryKey(user.getId());
        if (StringUtils.isNotEmpty(user.getPassword()) && StringUtils.isEmpty(user.getSalt())) {
            String pwd = user.getPassword();
            entryptPassword(user);

            // 保存htpasswd
            ShellUtil.exec("htpasswd -b", htpasswdPath + "/gitlab.htpasswd", user.getUsername(), pwd);
        } else {
            user.setPassword(null);
            user.setSalt(null);
        }

        if (StringUtils.isNotEmpty(user.getUsername()) && !oldUser.getUsername().equals(user.getUsername())) {
            // 更新项目的命名空间
            projectService.updateProjectNamespace(oldUser.getUsername(), user.getUsername());
        }

        baseMapper.updateByPrimaryKeySelective(user);

    }

    @Override
    @MethodLog
    public Long sendResetPasswordEmail(String toEmail) throws Exception {
        EmailTemplate emailTemplate = emailTemplateService.findEmailTemplateByCode(EmailTemplateCode.RESET_PASSWORD.getCode());

        Email email = new Email();
        email.setTemplateCode(emailTemplate.getCode());
        email.setTemplateName(emailTemplate.getName());

        String verifyCode = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 4);
        JSONObject params = new JSONObject();
        params.put("verifyCode", verifyCode);
        params.put("expiresIn", expiresIn);

        email.setToEmail(toEmail);
        email.setFromEmail(username);
        email.setParams(params.toJSONString());
        email.setSubject("【" + appName + "】" + emailTemplate.getName());
        email.setContent("【" + appName + "】" + String.format(emailTemplate.getTemplate(), verifyCode, expiresIn));

        emailService.send(email);
        return email.getId();
    }

    @Override
    @MethodLog
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(String email, String password) throws Exception {
        User user = new User();
        user.setPassword(password);
        entryptPassword(user);

        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("email", email);
        baseMapper.updateByExampleSelective(user, example);

        // 保存htpasswd
        ShellUtil.exec("htpasswd -b", htpasswdPath + "/gitlab.htpasswd", user.getUsername(), password);
    }

    @Override
    public List<User> searchUsers(UserRequest request) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();

        String username = request.getUsername();
        if (StringUtils.isNotEmpty(username)) {
            criteria.andLike("username", StringUtil.toLike(username));
        }

        String email = request.getEmail();
        if (StringUtils.isNotEmpty(email)) {
            criteria.andLike("email", StringUtil.toLike(email));
        }

        String fullName = request.getFullName();
        if (StringUtils.isNotEmpty(fullName)) {
            criteria.andLike("fullName", StringUtil.toLike(fullName));
        }

        sortAndPage(request, example);
        return baseMapper.selectByExample(example);
    }

    @Override
    @MethodLog
    public User getUser(Long id) {
        return baseMapper.selectByPrimaryKey(id);
    }

    @Override
    @MethodLog
    @Transactional(rollbackFor = Exception.class)
    public void removeUser(Long id) throws Exception {
        User user = baseMapper.selectByPrimaryKey(id);

        baseMapper.deleteByPrimaryKey(id);

        // 删除自己是唯一owner的组
        groupService.removeOnlyOwnerGroups(id);

        // 删除用户所在组
        groupUserService.removeUserGroups(id);

        // 项目用户的项目
        projectService.removeProjectByNamespace(user.getUsername());

        // 保存htpasswd
        ShellUtil.exec("htpasswd -D", htpasswdPath + "/gitlab.htpasswd", user.getUsername());
    }

    @Override
    @MethodLog
    public List<User> findUsersWithoutGroup(Long groupId) {
        return userMapper.selectUsersWithoutGroup(groupId);
    }

    @Override
    @MethodLog
    public List<User> findUsersWithoutProject(Long projectId) {
        return userMapper.selectUsersWithoutProject(projectId);
    }

    @Override
    public List<User> findAllUsers() {
        return baseMapper.selectAll();
    }

    /**
     * 设定安全的密码，生成随机的salt并经过N次 sha-1 hash
     *
     * @param user
     */
    private void entryptPassword(User user) {
        byte[] salt = Digests.generateSalt(AppConstants.SALT_SIZE);
        user.setSalt(Encodes.encodeHex(salt));

        byte[] hashPassword = Digests.sha1(user.getPassword().getBytes(), salt, AppConstants.HASH_INTERATIONS);
        user.setPassword(Encodes.encodeHex(hashPassword));
    }
}
