package com.kangyonggan.gitlab.interceptor;

import com.kangyonggan.gitlab.annotation.PermissionAccessLevel;
import com.kangyonggan.gitlab.annotation.PermissionLogin;
import com.kangyonggan.gitlab.constants.AccessLevel;
import com.kangyonggan.gitlab.constants.AppConstants;
import com.kangyonggan.gitlab.constants.Resp;
import com.kangyonggan.gitlab.dto.Response;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * 登录认证、身份认证
 *
 * @author kyg
 */
@Log4j2
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;

            // 校验登录权限注解
            if (!validLogin(response, handlerMethod)) {
                return false;
            }

            // 校验权限级别注解
            if (!validAccessLevel(response, handlerMethod)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 校验权限级别注解
     *
     * @param response
     * @param handlerMethod
     * @return
     */
    private boolean validAccessLevel(HttpServletResponse response, HandlerMethod handlerMethod) throws IOException {
        PermissionAccessLevel permissionAccessLevel = handlerMethod.getMethodAnnotation(PermissionAccessLevel.class);
        if (permissionAccessLevel != null) {
            if (!isLogin(response)) {
                return false;
            }

            AccessLevel accessLevel = AccessLevel.valueOf(ParamsInterceptor.getUser().getAccessLevel());
            if (!Arrays.asList(permissionAccessLevel.value()).contains(accessLevel)) {
                // 9997: 权限不足
                writePermissionDeniedResponse(response);
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否登录
     *
     * @param response
     * @return
     * @throws
     */
    private boolean isLogin(HttpServletResponse response) throws IOException {
        // 判断是否登录
        if (!ParamsInterceptor.getSession().getId().equals(ParamsInterceptor.getToken())) {
            // 9998: 登录失效
            Response resp = new Response();
            resp.failure(Resp.INVALID_LOGIN.getRespCo(), Resp.INVALID_LOGIN.getRespMsg());
            writeResponse(response, resp);
            return false;
        }
        return true;
    }

    /**
     * 校验登录权限注解
     *
     * @param response
     * @param handlerMethod
     * @return
     * @throws
     */
    private boolean validLogin(HttpServletResponse response, HandlerMethod handlerMethod) throws IOException {
        PermissionLogin permissionLogin = handlerMethod.getMethodAnnotation(PermissionLogin.class);
        if (permissionLogin != null) {
            // 判断是否登录
            if (!isLogin(response)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 写权限不足的响应
     *
     * @param response
     * @throws
     */
    private void writePermissionDeniedResponse(HttpServletResponse response) throws IOException {
        // 9997: 权限不足
        Response resp = new Response();
        resp.failure(Resp.PERMISSION_DENIED.getRespCo(), Resp.PERMISSION_DENIED.getRespMsg());
        writeResponse(response, resp);
    }

    /**
     * 写响应
     *
     * @param response
     * @param resp
     * @throws
     */
    private void writeResponse(HttpServletResponse response, Response resp) throws IOException {
        response.setCharacterEncoding(AppConstants.DEFAULT_CHARSET);

        try (PrintWriter writer = response.getWriter()) {
            writer.write(resp.toString());
            writer.flush();
        }
    }
}
