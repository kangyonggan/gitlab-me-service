package com.kangyonggan.gitlab.service;

import com.kangyonggan.gitlab.dto.ProjectUserDto;
import com.kangyonggan.gitlab.model.ProjectUser;

import java.util.List;

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

    /**
     * 查询项目成员
     *
     * @param projectId
     * @return
     */
    List<ProjectUserDto> findProjectUsers(Long projectId);

}
