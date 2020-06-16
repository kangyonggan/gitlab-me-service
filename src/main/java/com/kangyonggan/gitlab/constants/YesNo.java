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
    YES((byte) 1),

    /**
     * 否
     */
    NO((byte) 0);

    /**
     * 代码
     */
    @Getter
    private final byte code;

    YesNo(byte code) {
        this.code = code;
    }
}