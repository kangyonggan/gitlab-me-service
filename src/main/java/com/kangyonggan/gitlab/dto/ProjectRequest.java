package com.kangyonggan.gitlab.dto;

import lombok.Data;

/**
 * @author kyg
 */
@Data
public class ProjectRequest extends PageRequest {

    private String projectPath;

    private String projectName;

    private String namespace;

}
