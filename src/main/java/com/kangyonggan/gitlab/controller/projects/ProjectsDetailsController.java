package com.kangyonggan.gitlab.controller.projects;

import com.kangyonggan.gitlab.annotation.PermissionAccessLevel;
import com.kangyonggan.gitlab.constants.AccessLevel;
import com.kangyonggan.gitlab.controller.BaseController;
import com.kangyonggan.gitlab.dto.BlobInfo;
import com.kangyonggan.gitlab.dto.ProjectInfo;
import com.kangyonggan.gitlab.dto.Response;
import com.kangyonggan.gitlab.dto.TreeInfo;
import com.kangyonggan.gitlab.interceptor.ParamsInterceptor;
import com.kangyonggan.gitlab.service.ProjectService;
import com.kangyonggan.gitlab.util.ShellUtil;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * @author kyg
 */
@RestController
@RequestMapping("projects")
public class ProjectsDetailsController extends BaseController {

    @Autowired
    private ProjectService projectService;

    @Value("${gitlab.project-root}")
    private String projectRoot;

    /**
     * 目录详情
     *
     * @param namespace
     * @param projectPath
     * @param branch
     * @param fullPath
     * @return
     * @throws Exception
     */
    @GetMapping("{namespace:[\\w]+}/{projectPath:[\\w]+}/tree")
    @PermissionAccessLevel(AccessLevel.Admin)
    public Response tree(@PathVariable String namespace, @PathVariable String projectPath,
                         @RequestParam(required = false, defaultValue = "master") String branch,
                         @RequestParam(required = false, defaultValue = "./") String fullPath) throws Exception {
        Response response = successResponse();
        ProjectInfo project = projectService.findProjectInfo(namespace, projectPath, branch);
        List<TreeInfo> treeInfos = projectService.getProjectTree(namespace, projectPath, branch, fullPath);
        Map<String, Object> lastCommit = projectService.getLastCommit(namespace, projectPath, branch, fullPath);

        response.put("project", project);
        response.put("treeInfos", treeInfos);
        response.put("lastCommit", lastCommit);
        return response;
    }

    /**
     * 文件详情
     *
     * @param namespace
     * @param projectPath
     * @param branch
     * @param fullPath
     * @return
     * @throws Exception
     */
    @GetMapping("{namespace:[\\w]+}/{projectPath:[\\w]+}/blob")
    @PermissionAccessLevel(AccessLevel.Admin)
    public Response blob(@PathVariable String namespace, @PathVariable String projectPath,
                         @RequestParam(required = false, defaultValue = "master") String branch,
                         @RequestParam String fullPath) throws Exception {
        Response response = successResponse();
        ProjectInfo project = projectService.findProjectInfo(namespace, projectPath, branch);
        BlobInfo blobInfo = projectService.getProjectBlob(namespace, projectPath, branch, fullPath);

        response.put("project", project);
        response.put("blobInfo", blobInfo);
        return response;
    }

    /**
     * 文件下载
     *
     * @param namespace
     * @param projectPath
     * @param branch
     * @param fullPath
     * @throws Exception
     */
    @GetMapping("{namespace:[\\w]+}/{projectPath:[\\w]+}/raw")
    public void raw(@PathVariable String namespace, @PathVariable String projectPath,
                    @RequestParam(required = false, defaultValue = "master") String branch,
                    @RequestParam String fullPath) throws Exception {
        HttpServletResponse response = ParamsInterceptor.getResponse();
        response.setHeader("content-disposition", "attachement;filename=" + URLEncoder.encode(FilenameUtils.getName(fullPath), "UTF-8"));

        String cmd = "git --git-dir " + projectRoot + "/" + namespace + "/" + projectPath + ".git show " + branch + ":" + fullPath;
        try (InputStream in = ShellUtil.execStream(cmd);
             BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream())) {
            IOUtils.copyLarge(in, out);
        }
    }

    /**
     * 创建分支
     *
     * @param namespace
     * @param projectPath
     * @param branchName
     * @param createFrom
     * @return
     * @throws Exception
     */
    @PostMapping("{namespace:[\\w]+}/{projectPath:[\\w]+}/branch")
    @PermissionAccessLevel(AccessLevel.Admin)
    public Response branch(@PathVariable String namespace, @PathVariable String projectPath,
                        @RequestParam String branchName, @RequestParam String createFrom) throws Exception {
        projectService.newBranch(namespace, projectPath, branchName, createFrom);
        return successResponse();
    }

}
