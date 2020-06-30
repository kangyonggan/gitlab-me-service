package com.kangyonggan.gitlab.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 目录信息
 *
 * @author kyg
 */
@Data
public class TreeInfo implements Serializable {

    /**
     * 文件类型。blob：文件，tree：文件夹
     */
    private String type;

    /**
     * tree-ish（类似文件ID）
     */
    private String ish;

    /**
     * 全名
     */
    private String fullName;

    /**
     * 大小（byte）
     */
    private Long size;

    /**
     * 最后一次提交
     */
    private Map<String, Object> lastCommit;

}
