package com.kangyonggan.gitlab.constants;

import lombok.Getter;

/**
 * 是/否
 *
 * @author kyg
 */
public enum YesNo {

    /**
     * 是
     */
    YES((byte) 1, "是"),

    /**
     * 否
     */
    NO((byte) 0, "否");

    /**
     * 代码
     */
    @Getter
    private final byte code;

    /**
     * 名称
     */
    @Getter
    private final String name;

    YesNo(byte code, String name) {
        this.code = code;
        this.name = name;
    }
}