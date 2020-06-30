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
     * 文件全名
     */
    private String fullName;

    /**
     * 内容
     */
    private String content;
}
