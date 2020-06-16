package com.kangyonggan.gitlab.controller;

import com.kangyonggan.gitlab.constants.AppConstants;
import com.kangyonggan.gitlab.dto.Response;
import com.kangyonggan.gitlab.service.GroupService;
import com.kangyonggan.gitlab.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kyg
 */
@RestController
@RequestMapping("validate")
public class ValidateController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    /**
     * 校验是否是保留字
     *
     * @param word
     * @return
     */
    @GetMapping("reserved")
    public Response reserved(@RequestParam String word) {
        Response response = successResponse();

        if (AppConstants.RESERVED_WORDS.contains(word)) {
            response.failure(word + " is a reserved name");
        }

        return response;
    }

    /**
     * 校验用户名是否存在
     *
     * @param username
     * @return
     */
    @GetMapping("username")
    public Response username(@RequestParam String username) {
        Response response = successResponse();

        if (userService.existsUsername(username)) {
            response.failure("The username already exists");
        }

        return response;
    }

    /**
     * 校验组路径是否存在
     *
     * @param groupPath
     * @return
     */
    @GetMapping("groupPath")
    public Response groupPath(@RequestParam String groupPath) {
        Response response = successResponse();

        if (groupService.existsGroupPath(groupPath)) {
            response.failure("The group path already exists");
        }

        return response;
    }

    /**
     * 校验电子邮箱是否存在
     *
     * @param email
     * @return
     */
    @GetMapping("email")
    public Response email(@RequestParam String email) {
        Response response = successResponse();

        if (userService.existsEmail(email)) {
            response.failure("The email already exists");
        }

        return response;
    }

    /**
     * 校验电子邮箱是否不存在
     *
     * @param email
     * @return
     */
    @GetMapping("emailNotExists")
    public Response emailNotExists(@RequestParam String email) {
        Response response = successResponse();

        if (!userService.existsEmail(email)) {
            response.failure("The email not exists");
        }

        return response;
    }

}
