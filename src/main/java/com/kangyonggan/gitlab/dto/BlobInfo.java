package com.kangyonggan.gitlab.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

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
     * 内容是不是text
     */
    private boolean isText;

    /**
     * 内容
     */
    private String content;

    /**
     * 最后一次提交
     */
    private Map<String, Object> lastCommit;
}
