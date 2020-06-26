package com.kangyonggan.gitlab.dto;

import com.kangyonggan.gitlab.model.ProjectUser;
import lombok.Data;

import java.io.Serializable;

/**
 * @author kyg
 */
@Data
public class ProjectUserDto extends ProjectUser implements Serializable {

    private String fullName;

    private String username;

    private String avatar;

    private String email;

}
