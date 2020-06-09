package com.kangyonggan.gitlab.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * @author mbg
 */
@Data
public class User implements Serializable {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 全名
     */
    @Column(name = "full_name")
    private String fullName;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 密码
     */
    private String password;

    /**
     * 密码盐
     */
    private String salt;

    /**
     * 注册IP
     */
    @Column(name = "sign_up_ip")
    private String signUpIp;

    /**
     * 注册时间
     */
    @Column(name = "sign_up_time")
    private Date signUpTime;

    /**
     * 项目限定
     */
    @Column(name = "projects_limit")
    private Integer projectsLimit;

    /**
     * 是否可以创建组
     */
    @Column(name = "can_create_group")
    private Byte canCreateGroup;

    /**
     * 权限级别
     */
    @Column(name = "access_level")
    private String accessLevel;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 最后登录IP
     */
    @Column(name = "last_sign_in_ip")
    private String lastSignInIp;

    /**
     * 最后登录时间
     */
    @Column(name = "last_sign_in_time")
    private Date lastSignInTime;

    /**
     * 逻辑删除
     */
    @Column(name = "is_deleted")
    private Byte isDeleted;

    /**
     * 创建时间
     */
    @Column(name = "created_time")
    private Date createdTime;

    /**
     * 更新时间
     */
    @Column(name = "updated_time")
    private Date updatedTime;

    private static final long serialVersionUID = 1L;
}