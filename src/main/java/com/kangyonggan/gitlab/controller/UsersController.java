package com.kangyonggan.gitlab.controller;

import com.kangyonggan.gitlab.dto.Response;
import com.kangyonggan.gitlab.model.User;
import com.kangyonggan.gitlab.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kyg
 */
@RestController
@RequestMapping("users")
@Log4j2
public class UsersController extends BaseController {

    @Autowired
    private UserService userService;

    /**
     * 注册
     *
     * @param user
     * @return
     */
    @PostMapping("signUp")
    public Response signUp(User user) {
        user.setSignUpIp(getIpAddress());
        userService.saveUser(user);

        return successResponse();
    }
}
