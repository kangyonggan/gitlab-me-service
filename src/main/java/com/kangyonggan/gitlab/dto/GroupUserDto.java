package com.kangyonggan.gitlab.dto;

import com.kangyonggan.gitlab.model.GroupUser;
import lombok.Data;

import java.io.Serializable;

/**
 * @author kyg
 */
@Data
public class GroupUserDto extends GroupUser implements Serializable {

    private String fullName;

    private String username;

    private String avatar;

    private String email;

}
