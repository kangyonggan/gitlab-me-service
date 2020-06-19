package com.kangyonggan.gitlab.constants;


import lombok.Getter;

/**
 * @author kyg
 */
public enum Resp {

    /**
     * 操作成功
     */
    SUCCESS("0000", "success"),

    /**
     * 数据无效
     */
    INVALID_DATA("9996", "The data is invalid"),

    /**
     * 权限不足
     */
    PERMISSION_DENIED("9997", "You do not have access to the requested resource"),

    /**
     * 登录已失效
     */
    INVALID_LOGIN("9998", "You are not sign in or expired"),

    /**
     * 未知错误
     */
    FAILURE("9999", "Unknown error, please contact the administrator");

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
