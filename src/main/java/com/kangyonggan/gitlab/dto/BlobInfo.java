package com.kangyonggan.gitlab.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 文件信息
 *
 * @author kyg
 */
@Data
public class BlobInfo implements Serializable {

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
     * 内容
     */
    private String content;
}
