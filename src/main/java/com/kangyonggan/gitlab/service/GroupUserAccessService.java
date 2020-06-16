package com.kangyonggan.gitlab.service;

import com.kangyonggan.gitlab.model.GroupUserAccess;

/**
 * @author kyg
 */
public interface GroupUserAccessService {
    /**
     * 保存组用户权限
     *
     * @param access
     */
    void saveGroupUserAccess(GroupUserAccess access);

    /**
     * 删除组用户权限
     *
     * @param groupId
     */
    void removeGroupUserAccess(Long groupId);
}
