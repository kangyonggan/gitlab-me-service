package com.kangyonggan.gitlab.controller.admin;

import com.github.pagehelper.PageInfo;
import com.kangyonggan.gitlab.annotation.PermissionAccessLevel;
import com.kangyonggan.gitlab.constants.AccessLevel;
import com.kangyonggan.gitlab.controller.BaseController;
import com.kangyonggan.gitlab.dto.ProjectRequest;
import com.kangyonggan.gitlab.dto.Response;
import com.kangyonggan.gitlab.model.Group;
import com.kangyonggan.gitlab.model.Project;
import com.kangyonggan.gitlab.model.User;
import com.kangyonggan.gitlab.service.GroupService;
import com.kangyonggan.gitlab.service.ProjectService;
import com.kangyonggan.gitlab.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author kyg
 */
@RestController
@RequestMapping("admin/projects")
public class AdminProjectsController extends BaseController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService userService;

    @GetMapping
    @PermissionAccessLevel(AccessLevel.Admin)
    public Response list(ProjectRequest request) {
        Response response = new Response();

        List<Project> list = projectService.searchProjects(request);
        PageInfo<Project> pageInfo = new PageInfo<>(list);

        response.put("pageInfo", pageInfo);
        return response;
    }

    /**
     * 保存项目
     *
     * @param project
     * @return
     */
    @PostMapping
    @PermissionAccessLevel(AccessLevel.Admin)
    public Response save(Project project) {
        projectService.saveProject(project, currentUserId());
        return successResponse();
    }

    /**
     * 查询命名空间
     *
     * @return
     */
    @GetMapping("namespaces")
    @PermissionAccessLevel(AccessLevel.Admin)
    public Response namespaces() {
        Response response = successResponse();
        List<Group> groups = groupService.findAllGroups();
        List<User> users = userService.findAllUsers();

        response.put("groups", groups);
        response.put("users", users);
        return response;
    }

    /**
     * 查询项目
     *
     * @param projectPath
     * @return
     */
    @GetMapping("{projectPath:[\\w]+}")
    @PermissionAccessLevel(AccessLevel.Admin)
    public Response detail(@PathVariable String projectPath) {
        Response response = successResponse();
        response.put("project", projectService.findProjectByPath(projectPath));
        return response;
    }

    /**
     * 更新项目
     *
     * @param project
     * @return
     */
    @PutMapping
    @PermissionAccessLevel(AccessLevel.Admin)
    public Response update(Project project) {
        projectService.updateProject(project);
        return successResponse();
    }

    /**
     * 物理删除项目
     *
     * @param id
     * @return
     */
    @DeleteMapping("{id:[\\d]+}")
    @PermissionAccessLevel(AccessLevel.Admin)
    public Response delete(@PathVariable Long id) {
        projectService.removeProject(id);

        return successResponse();
    }

}
