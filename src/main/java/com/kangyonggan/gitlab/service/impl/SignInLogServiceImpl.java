package com.kangyonggan.gitlab.service.impl;

import com.kangyonggan.gitlab.annotation.MethodLog;
import com.kangyonggan.gitlab.model.SignInLog;
import com.kangyonggan.gitlab.service.BaseService;
import com.kangyonggan.gitlab.service.SignInLogService;
import org.springframework.stereotype.Service;

/**
 * @author kyg
 */
@Service
public class SignInLogServiceImpl extends BaseService<SignInLog> implements SignInLogService {

    @Override
    @MethodLog
    public void saveSignInLog(Long userId, String ipAddress) {
        SignInLog signInLog = new SignInLog();
        signInLog.setUserId(userId);
        signInLog.setSignInIp(ipAddress);

        baseMapper.insertSelective(signInLog);
    }
}
