package com.kangyonggan.gitlab.service;

import com.kangyonggan.gitlab.dto.UserRequest;
import com.kangyonggan.gitlab.model.User;

import java.util.List;

/**
 * @author kyg
 */
public interface UserService {

    /**
     * 校验用户名是否存在
     *
     * @param username
     * @return
     */
    boolean existsUsername(String username);

    /**
     * 校验电子邮箱是否存在
     *
     * @param email
     * @return
     */
    boolean existsEmail(String email);

    /**
     * 保存用户
     *
     * @param user
     */
    void saveUser(User user);

    /**
     * 根据用户名或者邮箱查找用户
     *
     * @param username
     * @return
     */
    User findUserByUsernameOrEmail(String username);

    /**
     * 更新用户
     *
     * @param user
     */
    void updateUser(User user);

    /**
     * 发重置密码邮件
     *
     * @param toEmail
     * @return
     * @throws Exception
     */
    Long sendResetPasswordEmail(String toEmail) throws Exception;

    /**
     * 重置密码
     *
     * @param email
     * @param password
     */
    void resetPassword(String email, String password);

    /**
     * 搜索用户
     *
     * @param request
     * @return
     */
    List<User> searchUsers(UserRequest request);

    /**
     * 查询用户
     *
     * @param id
     * @return
     */
    User getUser(Long id);

    /**
     * 删除用户
     *
     * @param id
     */
    void removeUser(Long id);
}
