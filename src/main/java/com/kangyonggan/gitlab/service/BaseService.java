package com.kangyonggan.gitlab.service;

import com.github.pagehelper.PageHelper;
import com.kangyonggan.gitlab.dto.PageRequest;
import com.kangyonggan.gitlab.extra.BaseMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

/**
 * 基础此service，可通过baseMapper调用单表的curd
 *
 * @author kyg
 */
public abstract class BaseService<T> {

    @Autowired
    protected BaseMapper<T> baseMapper;

    /**
     * 排序和分页
     *
     * @param request
     * @param example
     */
    protected void sortAndPage(PageRequest request, Example example) {
        String order = request.getOrder();
        if (StringUtils.isNotEmpty(order) && StringUtils.isNotEmpty(request.getProp())) {
            if (PageRequest.ASC.equals(order)) {
                example.orderBy(request.getProp()).asc();
            } else {
                example.orderBy(request.getProp()).desc();
            }
        } else {
            example.orderBy("id").desc();
        }

        PageHelper.startPage(request.getPageNum(), request.getPageSize());
    }

}
