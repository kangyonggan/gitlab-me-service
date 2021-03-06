package com.kangyonggan.gitlab.controller.admin;

import com.github.pagehelper.PageInfo;
import com.kangyonggan.gitlab.annotation.PermissionAccessLevel;
import com.kangyonggan.gitlab.constants.AccessLevel;
import com.kangyonggan.gitlab.controller.BaseController;
import com.kangyonggan.gitlab.dto.*;
import com.kangyonggan.gitlab.model.Group;
import com.kangyonggan.gitlab.model.Project;
import com.kangyonggan.gitlab.model.User;
import com.kangyonggan.gitlab.service.*;
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
    private ProjectUserService projectUserService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupUserService groupUserService;

    @GetMapping
    @PermissionAccessLevel(AccessLevel.Admin)
    public Response list(ProjectRequest request) {
        Response response = successResponse();

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
     * @throws Exception
     */
    @PostMapping
    @PermissionAccessLevel(AccessLevel.Admin)
    public Response save(Project project) throws Exception {
        projectService.saveProject(project, currentUserId());
        return successResponse();
    }

    /**
     * 查询全部命名空间
     *
     * @return
     */
    @GetMapping("allNamespaces")
    @PermissionAccessLevel(AccessLevel.Admin)
    public Response allNamespaces() {
        Response response = successResponse();
        List<Group> groups = groupService.findAllGroups();
        List<User> users = userService.findAllUsers();

        response.put("groups", groups);
        response.put("users", users);
        return response;
    }

    /**
     * 查询自己的命名空间
     *
     * @return
     */
    @GetMapping("namespaces")
    @PermissionAccessLevel(AccessLevel.Admin)
    public Response namespaces() {
        Response response = successResponse();
        List<Group> groups = groupService.findUserGroups(currentUserId());

        response.put("groups", groups);
        return response;
    }

    /**
     * 查询项目
     *
     * @param namespace
     * @param projectPath
     * @return
     */
    @GetMapping("{namespace:[\\w]+}/{projectPath:[\\w]+}")
    @PermissionAccessLevel(AccessLevel.Admin)
    public Response detail(@PathVariable String namespace, @PathVariable String projectPath) {
        Response response = successResponse();
        Project project = projectService.findProjectByNamespaceAndPath(namespace, projectPath);
        Group group = groupService.findGroupByPath(namespace);
        if (group == null) {
            User user = userService.findUserByUsernameOrEmail(namespace);
            response.put("user", user);
        } else {
            response.put("group", group);
        }

        response.put("project", project);
        return response;
    }

    /**
     * 查询项目和成员
     *
     * @param namespace
     * @param projectPath
     * @return
     * @throws Exception
     */
    @GetMapping("{namespace:[\\w]+}/{projectPath:[\\w]+}/users")
    @PermissionAccessLevel(AccessLevel.Admin)
    public Response detailWithUsers(@PathVariable String namespace, @PathVariable String projectPath) throws Exception {
        Response response = successResponse();
        ProjectInfo project = projectService.findProjectInfo(namespace, projectPath, "master");
        List<ProjectUserDto> projectUsers = projectUserService.findProjectUsers(project.getId());

        Group group = groupService.findGroupByPath(namespace);
        if (group != null) {
            List<GroupUserDto> groupUsers = groupUserService.findGroupUsers(group.getId());
            response.put("groupUsers", groupUsers);
            response.put("group", group);
        }

        response.put("project", project);
        response.put("projectUsers", projectUsers);
        return response;
    }

    /**
     * 更新项目
     *
     * @param project
     * @return
     * @throws Exception
     */
    @PutMapping
    @PermissionAccessLevel(AccessLevel.Admin)
    public Response update(Project project) throws Exception {
        projectService.updateProject(project);
        return successResponse();
    }

    /**
     * 物理删除项目
     *
     * @param id
     * @return
     * @throws Exception
     */
    @DeleteMapping("{id:[\\d]+}")
    @PermissionAccessLevel(AccessLevel.Admin)
    public Response delete(@PathVariable Long id) throws Exception {
        projectService.removeProject(id);

        return successResponse();
    }

}
