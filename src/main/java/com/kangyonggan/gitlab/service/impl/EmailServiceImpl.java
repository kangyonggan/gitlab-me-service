package com.kangyonggan.gitlab.service.impl;

import com.kangyonggan.gitlab.annotation.MethodLog;
import com.kangyonggan.gitlab.constants.YesNo;
import com.kangyonggan.gitlab.model.Email;
import com.kangyonggan.gitlab.service.BaseService;
import com.kangyonggan.gitlab.service.EmailService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import java.security.Security;
import java.util.Properties;

/**
 * @author kyg
 */
@Service
@Log4j2
public class EmailServiceImpl extends BaseService<Email> implements EmailService {

    private Properties props;

    @Value("${app.email.host}")
    private String host;

    @Value("${app.email.port}")
    private String port;

    @Value("${app.email.username}")
    private String username;

    @Value("${app.email.password}")
    private String password;

    /**
     * 发送邮件
     *
     * @param email
     * @throws Exception
     */
    @Override
    @MethodLog
    public void send(Email email) throws Exception {
        initProperties();
        MimeMessage msg = new MimeMessage(getSession());
        MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
        helper.setFrom(email.getFromEmail());
        helper.setTo(email.getToEmail());
        helper.setSubject(email.getSubject());
        helper.setText(email.getContent());
        Transport.send(msg);

        // 落库
        baseMapper.insertSelective(email);
        log.info("给{}发送邮件成功", email.getToEmail());
    }

    @Override
    @MethodLog
    public Email getEmail(Long emailId) {
        return baseMapper.selectByPrimaryKey(emailId);
    }

    @Override
    @MethodLog
    public void deleteEmail(Long emailId) {
        Email email = new Email();
        email.setId(emailId);
        email.setIsDeleted(YesNo.YES.getCode());
        baseMapper.updateByPrimaryKeySelective(email);
    }

    private void initProperties() {
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        if (props == null) {
            props = new Properties();
            props.setProperty("mail.smtp.host", host);
            props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.smtp.socketFactory.fallback", "false");
            props.setProperty("mail.smtp.port", port);
            props.setProperty("mail.smtp.socketFactory.port", port);
            props.put("mail.smtp.auth", "true");
        }
    }

    private Session getSession() {
        return Session.getDefaultInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }
}
