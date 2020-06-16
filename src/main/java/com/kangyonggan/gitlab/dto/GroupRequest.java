package com.kangyonggan.gitlab.dto;

import lombok.Data;

/**
 * @author kyg
 */
@Data
public class GroupRequest extends PageRequest {

    private String groupPath;

    private String groupName;

}
