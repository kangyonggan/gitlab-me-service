package com.kangyonggan.gitlab.constants;

import lombok.Getter;

/**
 * 权限
 *
 * @author kyg
 */
public enum Access {

    /**
     * 访客（可以new issues、pull request）
     */
    Guest((byte) 0),

    /**
     * 报告者（可以clone，不可以push）
     */
    Reporter((byte) 1),

    /**
     * 开发者（可以push、但不能push到master）
     */
    Developer((byte) 2),

    /**
     * 主分支权限（可以push到master）
     */
    Master((byte) 3),

    /**
     * 拥有者（全部权限）
     */
    Owner((byte) 4);

    /**
     * 代码
     */
    @Getter
    private final byte code;

    Access(byte code) {
        this.code = code;
    }

}