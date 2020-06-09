package com.kangyonggan.gitlab.constants;

import lombok.Getter;

/**
 * 权限级别
 *
 * @author kyg
 */
public enum AccessLevel {

    /**
     * 普通用户
     */
    Regular("Regular", "普通用户"),

    /**
     * 管理员
     */
    Admin("Admin", "管理员");

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

    AccessLevel(String code, String name) {
        this.code = code;
        this.name = name;
    }
}