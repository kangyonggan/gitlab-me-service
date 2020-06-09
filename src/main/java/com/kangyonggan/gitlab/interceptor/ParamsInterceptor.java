package com.kangyonggan.gitlab.interceptor;

import com.kangyonggan.gitlab.constants.AppConstants;
import com.kangyonggan.gitlab.model.User;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * 处理请求
 *
 * @author kyg
 */
@Log4j2
public class ParamsInterceptor extends HandlerInterceptorAdapter {

    /**
     * 存放当前请求
     */
    private static ThreadLocal<HttpServletRequest> currentRequest = new ThreadLocal<>();

    /**
     * 存放当前响应
     */
    private static ThreadLocal<HttpServletResponse> currentResponse = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 保存当前请求
        currentRequest.set(request);
        // 保存当前响应
        currentResponse.set(response);

        // 给log4j2设置线程变量uuid
        ThreadContext.put("uuid", UUID.randomUUID().toString().replaceAll("-", ""));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 从本地线程中移除请求
        currentRequest.remove();
        // 从本地线程中移除响应
        currentResponse.remove();

        // 移除log4j2的线程变量uuid
        ThreadContext.remove("uuid");
    }

    /**
     * 获取当前请求
     *
     * @return 当前请求
     */
    public static HttpServletRequest getRequest() {
        return currentRequest.get();
    }

    /**
     * 获取当前响应
     *
     * @return 当前响应
     */
    public static HttpServletResponse getResponse() {
        return currentResponse.get();
    }

    /**
     * 获取当前会话
     *
     * @return 当前请求
     */
    public static HttpSession getSession() {
        return currentRequest.get().getSession();
    }

    /**
     * 获取token
     *
     * @return
     */
    public static String getToken() {
        return currentRequest.get().getHeader(AppConstants.HEADER_TOKEN_NAME);
    }

    /**
     * 获取当前用户
     *
     * @return
     */
    public static User getUser() {
        return (User) getSession().getAttribute(AppConstants.KEY_SESSION_USER);
    }

}