package com.kangyonggan.gitlab.service;

import com.kangyonggan.gitlab.model.ProjectUser;

/**
 * @author kyg
 */
public interface ProjectUserService {

    /**
     * 保存项目成员
     *
     * @param projectUser
     */
    void saveProjectUser(ProjectUser projectUser);

    /**
     * 删除项目成员
     *
     * @param projectId
     */
    void removeProjectUsers(Long projectId);

    /**
     * 查询项目访问权限
     *
     * @param namespace
     * @param projectPath
     * @param username
     * @return
     */
    Byte findProjectAccess(String namespace, String projectPath, String username);
}
