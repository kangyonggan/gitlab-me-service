package com.kangyonggan.gitlab.annotation;

import com.kangyonggan.gitlab.constants.AccessLevel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限级别
 *
 * @author kyg
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionAccessLevel {

    /**
     * 权限级别
     *
     * @return
     */
    AccessLevel[] value();
}
