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

    /**
     * 删除命名空间下的项目用户
     *
     * @param namespace
     */
    void removeProjectUsers(String namespace);

    /**
     * 删除项目成员
     *
     * @param projectId
     * @param projectUserId
     * @return
     */
    boolean removeProjectUser(Long projectId, Long projectUserId);

    /**
     * 更新项目成员信息
     *
     * @param projectId
     * @param projectUserId
     * @param access
     * @param expirationDate
     * @return
     * @throws Exception
     */
    boolean updateProjectUser(Long projectId, Long projectUserId, byte access, String expirationDate) throws Exception;

    /**
     * 批量添加项目成员
     *
     * @param projectId
     * @param access
     * @param userIds
     * @param expirationDate
     */
    void saveProjectUsers(Long projectId, byte access, Long[] userIds, String expirationDate);
}
