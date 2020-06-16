package com.kangyonggan.gitlab.constants;

/**
 * 权限
 *
 * @author kyg
 */
public enum Access {

    /**
     * 访客（可以new issues、pull request）
     */
    Guest,

    /**
     * 报告者（可以clone，不可以push）
     */
    Reporter,

    /**
     * 开发者（可以push、但不能push到master）
     */
    Developer,

    /**
     * 主分支权限（可以push到master）
     */
    Master,

    /**
     * 拥有者（全部权限）
     */
    Owner;

}