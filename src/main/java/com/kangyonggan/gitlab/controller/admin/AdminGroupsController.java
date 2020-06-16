package com.kangyonggan.gitlab.controller.admin;

import com.github.pagehelper.PageInfo;
import com.kangyonggan.gitlab.annotation.PermissionAccessLevel;
import com.kangyonggan.gitlab.constants.AccessLevel;
import com.kangyonggan.gitlab.controller.BaseController;
import com.kangyonggan.gitlab.dto.GroupRequest;
import com.kangyonggan.gitlab.dto.GroupUserDto;
import com.kangyonggan.gitlab.dto.Response;
import com.kangyonggan.gitlab.model.Group;
import com.kangyonggan.gitlab.service.GroupService;
import com.kangyonggan.gitlab.service.GroupUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author kyg
 */
@RestController
@RequestMapping("admin/groups")
public class AdminGroupsController extends BaseController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupUserService groupUserService;

    /**
     * 组列表查询
     *
     * @param request
     * @return
     */
    @GetMapping
    @PermissionAccessLevel(AccessLevel.Admin)
    public Response list(GroupRequest request) {
        Response response = successResponse();

        List<Group> list = groupService.searchGroups(request);
        PageInfo<Group> pageInfo = new PageInfo<>(list);

        response.put("pageInfo", pageInfo);
        return response;
    }

    /**
     * 保存组
     *
     * @param group
     * @return
     */
    @PostMapping
    @PermissionAccessLevel(AccessLevel.Admin)
    public Response save(Group group) {
        Response response = successResponse();
        group = groupService.saveGroup(group, currentUserId());

        response.put("group", group);
        return response;
    }

    /**
     * 查询组
     *
     * @param id
     * @return
     */
    @GetMapping("{id:[\\d]+}")
    @PermissionAccessLevel(AccessLevel.Admin)
    public Response detail(@PathVariable Long id) {
        Response response = successResponse();
        response.put("group", groupService.getGroup(id));
        return response;
    }

    /**
     * 更新组
     *
     * @param group
     * @return
     */
    @PutMapping
    @PermissionAccessLevel(AccessLevel.Admin)
    public Response update(Group group) {
        Response response = successResponse();

        groupService.updateGroup(group);
        response.put("group", groupService.getGroup(group.getId()));

        return response;
    }

    /**
     * 物理删除组
     *
     * @param id
     * @return
     */
    @DeleteMapping("{id:[\\d]+}")
    @PermissionAccessLevel(AccessLevel.Admin)
    public Response delete(@PathVariable Long id) {
        groupService.removeGroup(id);

        return successResponse();
    }

    /**
     * 查询组用户
     *
     * @param id
     * @return
     */
    @GetMapping("{id:[\\d]+}/users")
    @PermissionAccessLevel(AccessLevel.Admin)
    public Response users(@PathVariable Long id) {
        Response response = successResponse();
        List<GroupUserDto> groupUsers = groupUserService.findGroupUsers(id);

        response.put("groupUsers", groupUsers);
        return response;
    }

    /**
     * 批量添加组用户
     *
     * @param id
     * @param access
     * @param userIds
     * @return
     */
    @PostMapping("{id:[\\d]+}/users")
    @PermissionAccessLevel(AccessLevel.Admin)
    public Response users(@PathVariable Long id, @RequestParam byte access, @RequestParam Long[] userIds) {
        groupUserService.saveGroupUsers(id, access, userIds);
        return successResponse();
    }

}
