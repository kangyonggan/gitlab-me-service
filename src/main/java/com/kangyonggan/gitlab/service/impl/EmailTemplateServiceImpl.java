package com.kangyonggan.gitlab.service.impl;

import com.kangyonggan.gitlab.annotation.MethodLog;
import com.kangyonggan.gitlab.model.EmailTemplate;
import com.kangyonggan.gitlab.service.BaseService;
import com.kangyonggan.gitlab.service.EmailTemplateService;
import org.springframework.stereotype.Service;

/**
 * @author kyg
 */
@Service
public class EmailTemplateServiceImpl extends BaseService<EmailTemplate> implements EmailTemplateService {

    @Override
    @MethodLog
    public EmailTemplate findEmailTemplateByCode(String code) {
        EmailTemplate emailTemplate = new EmailTemplate();
        emailTemplate.setCode(code);
        return baseMapper.selectOne(emailTemplate);
    }
}
