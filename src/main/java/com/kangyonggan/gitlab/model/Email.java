package com.kangyonggan.gitlab.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * @author mbg
 */
@Data
public class Email implements Serializable {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 模板代码
     */
    @Column(name = "template_code")
    private String templateCode;

    /**
     * 模板名称
     */
    @Column(name = "template_name")
    private String templateName;

    /**
     * 标题
     */
    private String subject;

    /**
     * 发送方
     */
    @Column(name = "from_email")
    private String fromEmail;

    /**
     * 接收方
     */
    @Column(name = "to_email")
    private String toEmail;

    /**
     * 参数
     */
    private String params;

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
     * 邮件内容
     */
    private String content;

    private static final long serialVersionUID = 1L;
}