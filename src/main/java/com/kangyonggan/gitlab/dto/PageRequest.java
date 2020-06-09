package com.kangyonggan.gitlab.dto;

import lombok.Data;

/**
 * @author kyg
 */
@Data
public class PageRequest extends Request {

    /**
     * 当前页
     */
    private Integer pageNum = 1;

    /**
     * 分页大小
     */
    private Integer pageSize = 10;

    /**
     * 排序的列
     */
    private String prop;

    /**
     * 排序顺序(asc: 升序，desc：降序)
     */
    private String order;

}
