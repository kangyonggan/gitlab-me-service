package com.kangyonggan.gitlab.controller.admin;

import com.github.pagehelper.PageInfo;
import com.kangyonggan.gitlab.annotation.PermissionAccessLevel;
import com.kangyonggan.gitlab.constants.AccessLevel;
import com.kangyonggan.gitlab.controller.BaseController;
import com.kangyonggan.gitlab.dto.Response;
import com.kangyonggan.gitlab.dto.UserRequest;
import com.kangyonggan.gitlab.model.User;
import com.kangyonggan.gitlab.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author kyg
 */
@RestController
@RequestMapping("admin/users")
public class AdminUsersController extends BaseController {

    @Autowired
    private UserService userService;

    /**
     * 用户列表查询
     *
     * @param request
     * @return
     */
    @GetMapping
    @PermissionAccessLevel(AccessLevel.Admin)
    public Response list(UserRequest request) {
        Response response = successResponse();

        List<User> list = userService.searchUsers(request);
        PageInfo<User> pageInfo = new PageInfo<>(list);

        response.put("pageInfo", pageInfo);
        return response;
    }

    /**
     * 查询除了指定组的用户
     *
     * @param groupId
     * @return
     */
    @GetMapping("withOutGroup/{groupId:[\\d]+}")
    @PermissionAccessLevel(AccessLevel.Admin)
    public Response withOutGroup(@PathVariable Long groupId) {
        Response response = successResponse();
        List<User> users = userService.findUsersWithoutGroup(groupId);

        response.put("users", users);
        return response;
    }

    /**
     * 保存用户
     *
     * @param user
     * @return
     */
    @PostMapping
    @PermissionAccessLevel(AccessLevel.Admin)
    public Response save(User user) {
        user.setSignUpIp(getIpAddress());
        userService.saveUser(user);
        return successResponse();
    }

    /**
     * 查询用户
     *
     * @param username
     * @return
     */
    @GetMapping("{username:[\\w]+}")
    @PermissionAccessLevel(AccessLevel.Admin)
    public Response detail(@PathVariable String username) {
        Response response = successResponse();
        response.put("user", userService.findUserByUsernameOrEmail(username));
        return response;
    }

    /**
     * 更新用户
     *
     * @param user
     * @return
     */
    @PutMapping
    @PermissionAccessLevel(AccessLevel.Admin)
    public Response update(User user) {
        userService.updateUser(user);
        return successResponse();
    }

    /**
     * 删除/恢复用户
     *
     * @param id
     * @param isDeleted
     * @return
     */
    @PutMapping("{id:[\\d]+}/delete/{isDeleted:\\b0\\b|\\b1\\b}")
    @PermissionAccessLevel(AccessLevel.Admin)
    public Response delete(@PathVariable Long id, @PathVariable Byte isDeleted) {
        User user = new User();
        user.setId(id);
        user.setIsDeleted(isDeleted);
        userService.updateUser(user);

        return successResponse();
    }

    /**
     * 物理删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("{id:[\\d]+}")
    @PermissionAccessLevel(AccessLevel.Admin)
    public Response delete(@PathVariable Long id) {
        userService.removeUser(id);

        return successResponse();
    }

}
