package com.kangyonggan.gitlab.controller.projects;

import com.kangyonggan.gitlab.annotation.PermissionAccessLevel;
import com.kangyonggan.gitlab.constants.AccessLevel;
import com.kangyonggan.gitlab.constants.Resp;
import com.kangyonggan.gitlab.controller.BaseController;
import com.kangyonggan.gitlab.dto.GroupUserDto;
import com.kangyonggan.gitlab.dto.ProjectUserDto;
import com.kangyonggan.gitlab.dto.Response;
import com.kangyonggan.gitlab.model.Group;
import com.kangyonggan.gitlab.model.Project;
import com.kangyonggan.gitlab.model.User;
import com.kangyonggan.gitlab.service.ProjectService;
import com.kangyonggan.gitlab.service.ProjectUserService;
import com.kangyonggan.gitlab.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author kyg
 */
@RestController
@RequestMapping("projects")
public class ProjectsUsersController extends BaseController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectUserService projectUserService;

    @GetMapping("{namespace:[\\w]+}/{projectPath:[\\w]+}/users")
    @PermissionAccessLevel(AccessLevel.Admin)
    public Response index(@PathVariable String namespace, @PathVariable String projectPath) {
        Response response = successResponse();

        Project project = projectService.findProjectByNamespaceAndPath(namespace, projectPath);
        if (project == null) {
            return response.failure(Resp.INVALID_DATA.getRespCo(), Resp.INVALID_DATA.getRespMsg());
        }
        List<User> users = userService.findUsersWithoutProject(project.getId());
        List<ProjectUserDto> projectUsers = projectUserService.findProjectUsers(project.getId());

        response.put("project", project);
        response.put("users", users);
        response.put("projectUsers", projectUsers);
        return response;
    }

    /**
     * 删除项目成员
     *
     * @param projectId
     * @param projectUserId
     * @return
     */
    @DeleteMapping("{projectId:[\\d]+}/users/{projectUserId:[\\d]+}")
    @PermissionAccessLevel(AccessLevel.Admin)
    public Response delete(@PathVariable Long projectId, @PathVariable Long projectUserId) {
        Response response = successResponse();
        if (!projectUserService.removeProjectUser(projectId, projectUserId)) {
            return response.failure();
        }

        return response;
    }

    /**
     * 修改成员信息
     *
     * @param projectId
     * @param projectUserId
     * @param access
     * @param expirationDate
     * @return
     * @throws Exception
     */
    @PutMapping("{projectId:[\\d]+}/users/{projectUserId:[\\d]+}")
    @PermissionAccessLevel(AccessLevel.Admin)
    public Response update(@PathVariable Long projectId, @PathVariable Long projectUserId, @RequestParam byte access,
                           @RequestParam(required = false) String expirationDate) throws Exception {
        Response response = successResponse();
        if (!projectUserService.updateProjectUser(projectId, projectUserId, access, expirationDate)) {
            return response.failure();
        }

        return response;
    }

    /**
     * 批量添加项目成员
     *
     * @param projectId
     * @param access
     * @param userIds
     * @param expirationDate
     * @return
     */
    @PostMapping("{projectId:[\\d]+}/users")
    @PermissionAccessLevel(AccessLevel.Admin)
    public Response users(@PathVariable Long projectId, @RequestParam byte access,
                          @RequestParam Long[] userIds, @RequestParam(required = false) String expirationDate) {
        projectUserService.saveProjectUsers(projectId, access, userIds, expirationDate);
        return successResponse();
    }

}
