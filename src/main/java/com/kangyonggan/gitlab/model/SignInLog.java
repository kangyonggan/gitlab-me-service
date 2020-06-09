package com.kangyonggan.gitlab.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * @author mbg
 */
@Data
@Table(name = "sign_in_log")
public class SignInLog implements Serializable {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 登录IP
     */
    @Column(name = "sign_in_ip")
    private String signInIp;

    /**
     * 登录时间
     */
    @Column(name = "sign_in_time")
    private Date signInTime;

    private static final long serialVersionUID = 1L;
}