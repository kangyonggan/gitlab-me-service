package com.kangyonggan.gitlab.service.impl;

import com.kangyonggan.gitlab.annotation.MethodLog;
import com.kangyonggan.gitlab.constants.AccessLevel;
import com.kangyonggan.gitlab.constants.AppConstants;
import com.kangyonggan.gitlab.constants.YesNo;
import com.kangyonggan.gitlab.model.User;
import com.kangyonggan.gitlab.service.BaseService;
import com.kangyonggan.gitlab.service.UserService;
import com.kangyonggan.gitlab.util.Digests;
import com.kangyonggan.gitlab.util.Encodes;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author kyg
 */
@Service
public class UserServiceImpl extends BaseService<User> implements UserService {

    @Override
    @MethodLog
    @Transactional(rollbackFor = Exception.class)
    public void saveUser(User user) {
        entryptPassword(user);

        user.setProjectsLimit(0);
        user.setCanCreateGroup(YesNo.NO.getCode());
        user.setAccessLevel(AccessLevel.Regular.getCode());
        user.setAvatar(null);

        user.setLastLoginIp(null);
        user.setLastLoginTime(null);
        user.setIsDeleted(YesNo.NO.getCode());
        user.setCreatedTime(null);
        user.setUpdatedTime(null);

        baseMapper.insertSelective(user);
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