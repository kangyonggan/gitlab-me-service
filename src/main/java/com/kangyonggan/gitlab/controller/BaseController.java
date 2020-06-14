package com.kangyonggan.gitlab.controller;

import com.kangyonggan.gitlab.constants.Resp;
import com.kangyonggan.gitlab.dto.Response;
import com.kangyonggan.gitlab.interceptor.ParamsInterceptor;
import com.kangyonggan.gitlab.model.User;
import com.kangyonggan.gitlab.util.IpUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author kyg
 */
@Log4j2
public class BaseController {

    /**
     * 异常捕获
     *
     * @param e 异常
     * @return 返回统一异常响应
     */
    @ExceptionHandler
    @ResponseBody
    public Response handleException(Exception e) {
        Response response = new Response();
        if (e != null) {
            log.error("捕获到异常", e);
            return response.failure(e.getMessage());
        }

        return response.failure();
    }

    /**
     * 生成一个成功响应
     *
     * @return
     */
    protected Response successResponse() {
        Response response = new Response();
        response.setRespCo(Resp.SUCCESS.getRespCo());
        response.setRespMsg(Resp.SUCCESS.getRespMsg());
        return response;
    }

    /**
     * 获取请求IP
     *
     * @return
     */
    protected String getIpAddress() {
        return IpUtil.getIpAddress(ParamsInterceptor.getRequest());
    }

    /**
     * 获取当前用户
     *
     * @return
     */
    protected User currentUser() {
        return ParamsInterceptor.getUser();
    }

    /**
     * 获取当前用户ID
     *
     * @return
     */
    protected Long currentUserId() {
        return ParamsInterceptor.getUser().getId();
    }

}
