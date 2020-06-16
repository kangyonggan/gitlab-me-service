package com.kangyonggan.gitlab.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * @author mbg
 */
@Data
@Table(name = "`group`")
public class Group implements Serializable {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 组路径
     */
    @Column(name = "group_path")
    private String groupPath;

    /**
     * 组名称
     */
    @Column(name = "group_name")
    private String groupName;

    /**
     * 描述
     */
    private String description;

    /**
     * 组头像
     */
    @Column(name = "group_avatar")
    private String groupAvatar;

    /**
     * 可见级别
     */
    @Column(name = "visibility_Level")
    private Byte visibilityLevel;

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