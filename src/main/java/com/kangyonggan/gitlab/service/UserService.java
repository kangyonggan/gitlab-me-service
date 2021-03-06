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
     * @throws Exception
     */
    void saveUser(User user) throws Exception;

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
     * @throws Exception
     */
    void updateUser(User user) throws Exception;

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
     * @throws Exception
     */
    void resetPassword(String email, String password) throws Exception;

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
     * @throws Exception
     */
    void removeUser(Long id) throws Exception;

    /**
     * 查询除了指定组的用户
     *
     * @param groupId
     * @return
     */
    List<User> findUsersWithoutGroup(Long groupId);

    /**
     * 查询除了指定项目的用户
     *
     * @param projectId
     * @return
     */
    List<User> findUsersWithoutProject(Long projectId);

    /**
     * 查询全部用户
     *
     * @return
     */
    List<User> findAllUsers();

}
