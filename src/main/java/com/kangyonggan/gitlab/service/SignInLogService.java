package com.kangyonggan.gitlab.service;

import com.kangyonggan.gitlab.model.SignInLog;

/**
 * @author kyg
 */
public interface SignInLogService {

    /**
     * 保存登录日志
     *
     * @param userId
     * @param ipAddress
     */
    void saveSignInLog(Long userId, String ipAddress);
}
