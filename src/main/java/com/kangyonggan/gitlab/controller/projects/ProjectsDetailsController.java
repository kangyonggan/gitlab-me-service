package com.kangyonggan.gitlab.controller.projects;

import com.kangyonggan.gitlab.annotation.PermissionAccessLevel;
import com.kangyonggan.gitlab.constants.AccessLevel;
import com.kangyonggan.gitlab.controller.BaseController;
import com.kangyonggan.gitlab.dto.ProjectInfo;
import com.kangyonggan.gitlab.dto.Response;
import com.kangyonggan.gitlab.dto.TreeInfo;
import com.kangyonggan.gitlab.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author kyg
 */
@RestController
@RequestMapping("projects")
public class ProjectsDetailsController extends BaseController {

    @Autowired
    private ProjectService projectService;

    /**
     * 项目详情
     *
     * @param namespace
     * @param projectPath
     * @return
     * @throws Exception
     */
    @GetMapping("{namespace:[\\w]+}/{projectPath:[\\w]+}")
    @PermissionAccessLevel(AccessLevel.Admin)
    public Response details(@PathVariable String namespace, @PathVariable String projectPath) throws Exception {
        Response response = successResponse();
        ProjectInfo project = projectService.findProjectInfo(namespace, projectPath);

        response.put("project", project);
        return response;
    }

    /**
     * 项目树信息
     *
     * @param namespace
     * @param projectPath
     * @param branch   分支
     * @param fullPath 全路径
     * @return
     * @throws Exception
     */
    @GetMapping("{namespace:[\\w]+}/{projectPath:[\\w]+}/tree")
    @PermissionAccessLevel(AccessLevel.Admin)
    public Response tree(@PathVariable String namespace, @PathVariable String projectPath,
                         @RequestParam(required = false, defaultValue = "master") String branch,
                         @RequestParam(required = false, defaultValue = "") String fullPath) throws Exception {
        Response response = successResponse();

        List<TreeInfo> treeInfos = projectService.getProjectTree(namespace, projectPath, branch, fullPath);
        response.put("treeInfos", treeInfos);

        return response;
    }

}
