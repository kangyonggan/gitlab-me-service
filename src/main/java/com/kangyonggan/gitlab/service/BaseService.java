package com.kangyonggan.gitlab.service;

import com.kangyonggan.gitlab.extra.BaseMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 基础此service，可通过baseMapper调用单表的curd
 *
 * @author kyg
 */
public abstract class BaseService<T> {

    @Autowired
    protected BaseMapper<T> baseMapper;

}
