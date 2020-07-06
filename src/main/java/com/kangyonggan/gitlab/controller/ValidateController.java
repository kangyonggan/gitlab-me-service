package com.kangyonggan.gitlab.controller;

import com.kangyonggan.gitlab.constants.Resp;
import com.kangyonggan.gitlab.dto.Response;
import com.kangyonggan.gitlab.model.Group;
import com.kangyonggan.gitlab.model.User;
import com.kangyonggan.gitlab.service.GroupService;
import com.kangyonggan.gitlab.service.ProjectService;
import com.kangyonggan.gitlab.service.UserService;
import com.kangyonggan.gitlab.util.ShellUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${gitlab.project-root}")
    private String projectRoot;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private ProjectService projectService;

    /**
     * 获取代码类型
     *
     * @param code
     * @return
     */
    @GetMapping("getCodeType")
    public Response getCodeType(@RequestParam String code) {
        Response response = successResponse();
        User user = userService.findUserByUsernameOrEmail(code);
        if (user != null) {
            response.put("type", "Users");
            response.put("item", user);
            return response;
        }

        Group group = groupService.findGroupByPath(code);
        if (group != null) {
            response.put("type", "Groups");
            response.put("item", group);
            return response;
        }

        return response.failure();
    }

    /**
     * 校验是否是唯一的
     *
     * @param code
     * @return
     */
    @GetMapping("uniqueCode")
    public Response uniqueCode(@RequestParam String code) {
        Response response = username(code);
        if (Resp.FAILURE.getRespCo().equals(response.getRespCo())) {
            return response.failure(code + " has already been taken");
        }

        response = groupPath(code);
        if (Resp.FAILURE.getRespCo().equals(response.getRespCo())) {
            return response.failure(code + " has already been taken");
        }

        return successResponse();
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
     * 校验项目路径是否存在
     *
     * @param namespace
     * @param projectPath
     * @return
     */
    @GetMapping("projectPath")
    public Response projectPath(@RequestParam String namespace, @RequestParam String projectPath) {
        Response response = successResponse();

        if (projectService.existsProjectPath(namespace, projectPath)) {
            response.failure("The project path already exists");
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

    /**
     * 校验文件是否不存在
     *
     * @param namespace
     * @param projectPath
     * @param branch
     * @param fullPath
     * @return
     * @throws Exception
     */
    @GetMapping("fileName")
    public Response fileName(@RequestParam String namespace, @RequestParam String projectPath,
                             @RequestParam String branch, @RequestParam String fullPath) throws Exception {
        Response response = successResponse();

        // git ls-tree master HEAD service/2.txt
        String result = ShellUtil.execSimple("git --git-dir " + projectRoot + "/" + namespace + "/" + projectPath + ".git ls-tree " + branch + " HEAD " + fullPath);
        if (StringUtils.isNotEmpty(result)) {
            response.failure("This file already exists");
        }

        return response;
    }

}
