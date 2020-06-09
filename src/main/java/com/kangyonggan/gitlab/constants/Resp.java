package com.kangyonggan.gitlab.constants;


import lombok.Getter;

/**
 * @author kyg
 */
public enum Resp {

    /**
     * 操作成功
     */
    SUCCESS("0000", "成功"),

    /**
     * 权限不足
     */
    PERMISSION_DENIED("9997", "您无权访问所请求的资源！"),

    /**
     * 登录已失效
     */
    INVALID_LOGIN("9998", "您尚未登录或登录已失效！"),

    /**
     * 未知错误
     */
    FAILURE("9999", "未知错误，请联系管理员！");

    /**
     * 响应码
     */
    @Getter
    String respCo;

    /**
     * 响应消息
     */
    @Getter
    String respMsg;

    Resp(String respCo, String respMsg) {
        this.respCo = respCo;
        this.respMsg = respMsg;
    }

}
