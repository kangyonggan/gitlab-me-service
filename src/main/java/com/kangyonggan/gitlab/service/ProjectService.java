package com.kangyonggan.gitlab.service;

import com.kangyonggan.gitlab.dto.BlobInfo;
import com.kangyonggan.gitlab.dto.ProjectInfo;
import com.kangyonggan.gitlab.dto.ProjectRequest;
import com.kangyonggan.gitlab.dto.TreeInfo;
import com.kangyonggan.gitlab.model.Project;
import com.kangyonggan.gitlab.model.User;

import java.util.List;
import java.util.Map;

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
     * 创建项目
     *
     * @param project
     * @param userId
     * @throws Exception
     */
    void saveProject(Project project, Long userId) throws Exception;

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
     * @throws Exception
     */
    void updateProject(Project project) throws Exception;

    /**
     * 移除项目
     *
     * @param id
     * @throws Exception
     */
    void removeProject(Long id) throws Exception;

    /**
     * 判断项目路径是否存在
     *
     * @param namespace
     * @param projectPath
     * @return
     */
    boolean existsProjectPath(String namespace, String projectPath);

    /**
     * 更新命名空间
     *
     * @param oldNamespace
     * @param namespace
     * @throws Exception
     */
    void updateProjectNamespace(String oldNamespace, String namespace) throws Exception;

    /**
     * 删除命名空间下的项目
     *
     * @param namespace
     * @throws Exception
     */
    void removeProjectByNamespace(String namespace) throws Exception;

    /**
     * 查找项目信息
     *
     * @param namespace
     * @param projectPath
     * @param branch
     * @return
     * @throws Exception
     */
    ProjectInfo findProjectInfo(String namespace, String projectPath, String branch) throws Exception;

    /**
     * 获取目录
     *
     * @param namespace
     * @param projectPath
     * @param branch
     * @param fullPath
     * @return
     * @throws Exception
     */
    List<TreeInfo> getProjectTree(String namespace, String projectPath, String branch, String fullPath) throws Exception;

    /**
     * 获取文件
     *
     * @param namespace
     * @param projectPath
     * @param branch
     * @param fullPath
     * @return
     * @throws Exception
     */
    BlobInfo getProjectBlob(String namespace, String projectPath, String branch, String fullPath) throws Exception;

    /**
     * 获取最近提交
     *
     * @param namespace
     * @param projectPath
     * @param branch
     * @param fullPath
     * @return
     * @throws Exception
     */
    Map<String, Object> getLastCommit(String namespace, String projectPath, String branch, String fullPath) throws Exception;

    /**
     * 创建分支
     *
     * @param namespace
     * @param projectPath
     * @param branchName
     * @param createFrom
     * @throws Exception
     */
    void newBranch(String namespace, String projectPath, String branchName, String createFrom) throws Exception;

    /**
     * 创建目录
     *
     * @param namespace
     * @param projectPath
     * @param branchName
     * @param parentPath
     * @param directoryName
     * @param commitMessage
     * @param user
     * @throws Exception
     */
    void newDir(String namespace, String projectPath, String branchName, String parentPath, String directoryName, String commitMessage, User user) throws Exception;

    /**
     * 创建文件
     *
     * @param namespace
     * @param projectPath
     * @param branchName
     * @param parentPath
     * @param fileName
     * @param content
     * @param contentType
     * @param commitMessage
     * @param user
     * @throws Exception
     */
    void newFile(String namespace, String projectPath, String branchName, String parentPath, String fileName, String content, String contentType, String commitMessage, User user) throws Exception;

    /**
     * 上传文件
     *
     * @param namespace
     * @param projectPath
     * @param branchName
     * @param parentPath
     * @param fileName
     * @param sourceFile
     * @param commitMessage
     * @param user
     * @throws Exception
     */
    void uploadFile(String namespace, String projectPath, String branchName, String parentPath, String fileName, String sourceFile, String commitMessage, User user) throws Exception;

    /**
     * 删除文件
     *
     * @param namespace
     * @param projectPath
     * @param branchName
     * @param fullPath
     * @param commitMessage
     * @param user
     * @throws Exception
     */
    void deleteFile(String namespace, String projectPath, String branchName, String fullPath, String commitMessage, User user) throws Exception;

    /**
     * 替换文件
     *
     * @param namespace
     * @param projectPath
     * @param branchName
     * @param fullPath
     * @param sourceFile
     * @param commitMessage
     * @param user
     * @throws Exception
     */
    void replaceFile(String namespace, String projectPath, String branchName, String fullPath, String sourceFile, String commitMessage, User user) throws Exception;

    /**
     * 更新文件
     *
     * @param namespace
     * @param projectPath
     * @param branchName
     * @param parentPath
     * @param fileName
     * @param oldFileName
     * @param content
     * @param contentType
     * @param commitMessage
     * @param user
     * @throws Exception
     */
    void updateFile(String namespace, String projectPath, String branchName, String parentPath, String fileName, String oldFileName, String content, String contentType, String commitMessage, User user) throws Exception;
}
