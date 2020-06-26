package com.kangyonggan.gitlab.controller.profile;

import com.kangyonggan.gitlab.annotation.PermissionLogin;
import com.kangyonggan.gitlab.controller.BaseController;
import com.kangyonggan.gitlab.dto.Response;
import com.kangyonggan.gitlab.model.User;
import com.kangyonggan.gitlab.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author kyg
 */
@RestController
@RequestMapping("profile")
public class ProfileController extends BaseController {

    @Autowired
    private UserService userService;

    /**
     * 更新个人信息
     *
     * @param avatar
     * @param fullName
     * @param email
     * @return
     * @throws Exception
     */
    @PutMapping
    @PermissionLogin
    public Response update(@RequestParam String avatar, @RequestParam String fullName, @RequestParam String email) throws Exception {
        Response response = successResponse();

        User user = new User();
        user.setId(currentUserId());
        user.setFullName(fullName);
        user.setEmail(email);
        user.setAvatar(avatar);

        userService.updateUser(user);
        user = userService.getUser(user.getId());

        response.put("user", user);
        return response;
    }

    /**
     * 删除头像
     *
     * @return
     * @throws Exception
     */
    @DeleteMapping("avatar")
    @PermissionLogin
    public Response removeAvatar() throws Exception {
        Response response = successResponse();

        User user = new User();
        user.setId(currentUserId());
        user.setAvatar("");

        userService.updateUser(user);
        user = userService.getUser(user.getId());

        response.put("user", user);
        return response;
    }
}
