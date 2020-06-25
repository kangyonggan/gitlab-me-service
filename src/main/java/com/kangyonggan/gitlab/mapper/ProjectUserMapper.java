package com.kangyonggan.gitlab.mapper;

import com.kangyonggan.gitlab.extra.BaseMapper;
import com.kangyonggan.gitlab.model.ProjectUser;
import org.apache.ibatis.annotations.Param;

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
}