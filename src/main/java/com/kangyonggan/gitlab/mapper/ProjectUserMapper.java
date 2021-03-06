package com.kangyonggan.gitlab.mapper;

import com.kangyonggan.gitlab.dto.ProjectUserDto;
import com.kangyonggan.gitlab.extra.BaseMapper;
import com.kangyonggan.gitlab.model.ProjectUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author mbg
 */
public interface ProjectUserMapper extends BaseMapper<ProjectUser> {
    /**
     * 查询项目访问权限
     *
     * @param namespace
     * @param projectPath
     * @param username
     * @return
     */
    Byte selectProjectAccess(@Param("namespace") String namespace, @Param("projectPath") String projectPath, @Param("username") String username);

    /**
     * 查询项目成员
     *
     * @param projectId
     * @return
     */
    List<ProjectUserDto> selectProjectUsers(Long projectId);

    /**
     * 删除命名空间下的项目用户
     *
     * @param namespace
     */
    void deleteProjectUsers(String namespace);

    /**
     * 批量添加项目成员
     *
     * @param projectId
     * @param access
     * @param userIds
     * @param expirationDate
     */
    void insertProjectUsers(@Param("projectId") Long projectId, @Param("access") byte access, @Param("userIds") Long[] userIds, @Param("expirationDate") String expirationDate);
}