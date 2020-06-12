package com.kangyonggan.gitlab.dto;

import lombok.Data;

/**
 * @author kyg
 */
@Data
public class UserRequest extends PageRequest {

    private String username;

    private String email;

    private String fullName;

}
