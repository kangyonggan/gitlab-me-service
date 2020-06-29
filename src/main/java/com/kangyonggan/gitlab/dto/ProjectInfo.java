package com.kangyonggan.gitlab.dto;

import com.kangyonggan.gitlab.model.Project;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
     * 最后提交时间
     */
    private Date lastCommitTime;

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
