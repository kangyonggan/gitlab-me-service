package com.kangyonggan.gitlab.service;

import com.kangyonggan.gitlab.dto.GroupUserDto;
import com.kangyonggan.gitlab.model.GroupUser;

import java.util.List;

/**
 * @author kyg
 */
public interface GroupUserService {
    /**
     * 保存组用户
     *
     * @param groupUser
     */
    void saveGroupUser(GroupUser groupUser);

    /**
     * 删除组用户
     *
     * @param groupId
     */
    void removeGroupUsers(Long groupId);

    /**
     * 批量保存组用户
     *
     * @param groupId
     * @param access
     * @param userIds
     * @param expirationDate
     */
    void saveGroupUsers(Long groupId, byte access, Long[] userIds, String expirationDate);

    /**
     * 查询组用户
     *
     * @param groupId
     * @return
     */
    List<GroupUserDto> findGroupUsers(Long groupId);

    /**
     * 删除组用户
     *
     * @param groupId
     * @param groupUserId
     * @return
     */
    boolean removeGroupUser(Long groupId, Long groupUserId);

    /**
     * 更新组用户
     *
     * @param groupId
     * @param groupUserId
     * @param access
     * @param expirationDate
     * @return
     * @throws Exception
     */
    boolean updateGroupUser(Long groupId, Long groupUserId, byte access, String expirationDate) throws Exception;

    /**
     * 删除用户所在组
     *
     * @param userId
     */
    void removeUserGroups(Long userId);

}
