package com.kangyonggan.gitlab.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author mbg
 */
@Data
@Table(name = "email_template")
public class EmailTemplate implements Serializable {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 模板代码
     */
    private String code;

    /**
     * 模板名称
     */
    private String name;

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

    /**
     * 邮件模板
     */
    private String template;

    private static final long serialVersionUID = 1L;
}