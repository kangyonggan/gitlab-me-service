package com.kangyonggan.gitlab.service;

import com.kangyonggan.gitlab.model.User;

/**
 * @author kyg
 */
public interface UserService {

    /**
     * 保存用户
     *
     * @param user
     */
    void saveUser(User user);

}
