package com.kangyonggan.gitlab.controller.groups;

import com.kangyonggan.gitlab.annotation.PermissionAccessLevel;
import com.kangyonggan.gitlab.constants.AccessLevel;
import com.kangyonggan.gitlab.constants.Resp;
import com.kangyonggan.gitlab.controller.BaseController;
import com.kangyonggan.gitlab.dto.GroupUserDto;
import com.kangyonggan.gitlab.dto.Response;
import com.kangyonggan.gitlab.model.Group;
import com.kangyonggan.gitlab.model.User;
import com.kangyonggan.gitlab.service.GroupService;
import com.kangyonggan.gitlab.service.GroupUserService;
import com.kangyonggan.gitlab.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author kyg
 */
@RestController
@RequestMapping("groups")
public class GroupsMembersController extends BaseController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupUserService groupUserService;

    @GetMapping("{groupPath:[\\w]+}/members")
    @PermissionAccessLevel(AccessLevel.Admin)
    public Response index(@PathVariable String groupPath) {
        Response response = successResponse();

        Group group = groupService.findGroupByPath(groupPath);
        if (group == null) {
            return response.failure(Resp.INVALID_DATA.getRespCo(), Resp.INVALID_DATA.getRespMsg());
        }
        List<User> users = userService.findUsersWithoutGroup(group.getId());
        List<GroupUserDto> groupUsers = groupUserService.findGroupUsers(group.getId());

        response.put("group", group);
        response.put("users", users);
        response.put("groupUsers", groupUsers);
        return response;
    }

    /**
     * 删除组成员
     *
     * @param groupId
     * @param groupUserId
     * @return
     */
    @DeleteMapping("{groupId:[\\d]+}/members/{groupUserId:[\\d]+}")
    @PermissionAccessLevel(AccessLevel.Admin)
    public Response delete(@PathVariable Long groupId, @PathVariable Long groupUserId) {
        Response response = successResponse();
        if (!groupUserService.removeGroupUser(groupId, groupUserId)) {
            return response.failure();
        }

        return response;
    }

    /**
     * 修改成员信息
     *
     * @param groupId
     * @param groupUserId
     * @param access
     * @param expirationDate
     * @return
     * @throws Exception
     */
    @PutMapping("{groupId:[\\d]+}/members/{groupUserId:[\\d]+}")
    @PermissionAccessLevel(AccessLevel.Admin)
    public Response update(@PathVariable Long groupId, @PathVariable Long groupUserId, @RequestParam byte access,
                           @RequestParam(required = false) String expirationDate) throws Exception {
        Response response = successResponse();
        if (!groupUserService.updateGroupUser(groupId, groupUserId, access, expirationDate)) {
            return response.failure();
        }

        return response;
    }

}
