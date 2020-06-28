package com.kangyonggan.gitlab.dto;

import com.kangyonggan.gitlab.model.Project;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author kyg
 */
@Data
public class ProjectInfo extends Project implements Serializable {

    /**
     * 占磁盘大小
     */
    private String size;

    /**
     * 最后提交时间
     */
    private Date lastCommitTime;

}
