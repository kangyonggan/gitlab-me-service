package com.kangyonggan.gitlab.controller.profile;

import com.github.pagehelper.PageInfo;
import com.kangyonggan.gitlab.annotation.PermissionLogin;
import com.kangyonggan.gitlab.controller.BaseController;
import com.kangyonggan.gitlab.dto.PageRequest;
import com.kangyonggan.gitlab.dto.Response;
import com.kangyonggan.gitlab.model.SignInLog;
import com.kangyonggan.gitlab.service.SignInLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author kyg
 */
@RestController
@RequestMapping("profile/signInLog")
public class ProfileSignInLogController extends BaseController {

    @Autowired
    private SignInLogService signInLogService;

    /**
     * 分页查询登录日志
     *
     * @param request
     * @return
     */
    @GetMapping
    @PermissionLogin
    public Response list(PageRequest request) {
        Response response = successResponse();

        List<SignInLog> list = signInLogService.searchSignInLog(request, currentUserId());
        PageInfo<SignInLog> pageInfo = new PageInfo<>(list);

        response.put("pageInfo", pageInfo);
        return response;
    }
}
