package com.kangyonggan.gitlab.dto;

import lombok.Data;

/**
 * @author kyg
 */
@Data
public class PageRequest extends Request {

    public static final String ASC = "ascending";
    public static final String DESC = "descending";

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
     * 排序顺序(ascending: 升序，descending：降序)
     */
    private String order;

}
