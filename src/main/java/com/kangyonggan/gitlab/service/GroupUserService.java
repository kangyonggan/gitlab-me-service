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
     * @param access
     */
    void saveGroupUser(GroupUser access);

    /**
     * 删除组用户
     *
     * @param groupId
     */
    void removeGroupUser(Long groupId);

    /**
     * 批量保存组用户
     *
     * @param groupId
     * @param access
     * @param userIds
     */
    void saveGroupUsers(Long groupId, byte access, Long[] userIds);

    /**
     * 查询组用户
     *
     * @param groupId
     * @return
     */
    List<GroupUserDto> findGroupUsers(Long groupId);

}
