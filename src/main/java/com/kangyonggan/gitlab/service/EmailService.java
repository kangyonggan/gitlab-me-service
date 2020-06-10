package com.kangyonggan.gitlab.service;

import com.kangyonggan.gitlab.model.Email;

/**
 * @author kyg
 */
public interface EmailService {

    /**
     * 发送邮件
     *
     * @param email
     * @throws Exception
     */
    void send(Email email) throws Exception;

    /**
     * 查找邮件
     *
     * @param emailId
     * @return
     */
    Email getEmail(Long emailId);

    /**
     * 删除邮件
     *
     * @param emailId
     */
    void deleteEmail(Long emailId);
}
