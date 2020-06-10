package com.kangyonggan.gitlab.service;

import com.kangyonggan.gitlab.model.EmailTemplate;

/**
 * @author kyg
 */
public interface EmailTemplateService {

    /**
     * 根据邮件模板代码查找模板
     *
     * @param code
     * @return
     */
    EmailTemplate findEmailTemplateByCode(String code);
}
