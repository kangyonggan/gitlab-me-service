package com.kangyonggan.gitlab.dto;

import com.kangyonggan.gitlab.model.Project;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author kyg
 */
@Data
public class ProjectInfo extends Project implements Serializable {

    /**
     * 占磁盘大小(KB)
     */
    private Long size;

    /**
     * 最后一次提交
     */
    private Map<String, Object> lastCommit;

    /**
     * 分支
     */
    private List<String> branches;

    /**
     * 标签
     */
    private List<String> tags;

    /**
     * 提交数量
     */
    private Integer commitNums;

}
