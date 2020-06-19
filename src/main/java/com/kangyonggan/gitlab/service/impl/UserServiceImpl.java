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
import com.kangyonggan.gitlab.service.BaseService;
import com.kangyonggan.gitlab.service.EmailService;
import com.kangyonggan.gitlab.service.EmailTemplateService;
import com.kangyonggan.gitlab.service.UserService;
import com.kangyonggan.gitlab.util.Digests;
import com.kangyonggan.gitlab.util.Encodes;
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
    public void saveUser(User user) {
        entryptPassword(user);

        user.setLastSignInIp(null);
        user.setLastSignInTime(null);
        user.setIsDeleted(YesNo.NO.getCode());
        user.setCreatedTime(null);
        user.setUpdatedTime(null);

        baseMapper.insertSelective(user);
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
    public void updateUser(User user) {
        if (StringUtils.isNotEmpty(user.getPassword()) && StringUtils.isEmpty(user.getSalt())) {
            entryptPassword(user);
        } else {
            user.setPassword(null);
            user.setSalt(null);
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
    public void resetPassword(String email, String password) {
        User user = new User();
        user.setPassword(password);
        entryptPassword(user);

        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("email", email);
        baseMapper.updateByExampleSelective(user, example);
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
    public void removeUser(Long id) {
        baseMapper.deleteByPrimaryKey(id);
    }

    @Override
    @MethodLog
    public List<User> findAllUsers() {
        return baseMapper.selectAll();
    }

    @Override
    @MethodLog
    public List<User> findUsersWithoutGroup(Long groupId) {
        return userMapper.selectUsersWithoutGroup(groupId);
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
