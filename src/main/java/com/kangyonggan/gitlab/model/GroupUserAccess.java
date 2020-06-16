package com.kangyonggan.gitlab.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * @author mbg
 */
@Data
@Table(name = "group_user_access")
public class GroupUserAccess implements Serializable {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 组ID
     */
    @Column(name = "group_id")
    private Long groupId;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 权限
     */
    private String access;

    /**
     * 失效日期
     */
    @Column(name = "expiration_date")
    private Date expirationDate;

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