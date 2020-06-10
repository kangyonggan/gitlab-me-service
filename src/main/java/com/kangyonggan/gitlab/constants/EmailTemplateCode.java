package com.kangyonggan.gitlab.constants;

import lombok.Getter;

/**
 * 邮件模板代码
 *
 * @author kyg
 */
public enum EmailTemplateCode {

    /**
     * 重置密码
     */
    RESET_PASSWORD("reset_password", "重置密码");

    /**
     * 代码
     */
    @Getter
    private final String code;

    /**
     * 名称
     */
    @Getter
    private final String name;

    EmailTemplateCode(String code, String name) {
        this.code = code;
        this.name = name;
    }
}