package com.kangyonggan.gitlab.service;

import com.kangyonggan.gitlab.dto.ProjectRequest;
import com.kangyonggan.gitlab.model.Project;

import java.util.List;

/**
 * @author kyg
 */
public interface ProjectService {

    /**
     * 搜索项目
     *
     * @param request
     * @return
     */
    List<Project> searchProjects(ProjectRequest request);

    /**
     * 保持项目
     *
     * @param project
     * @param userId
     */
    void saveProject(Project project, Long userId);

    /**
     * 查询项目
     *
     * @param projectPath
     * @return
     */
    Project findProjectByNamespaceAndPath(String namespace, String projectPath);

    /**
     * 更新项目
     *
     * @param project
     */
    void updateProject(Project project);

    /**
     * 移除项目
     *
     * @param id
     */
    void removeProject(Long id);

    /**
     * 判断项目路径是否存在
     *
     * @param namespace
     * @param projectPath
     * @return
     */
    boolean existsProjectPath(String namespace, String projectPath);
}
