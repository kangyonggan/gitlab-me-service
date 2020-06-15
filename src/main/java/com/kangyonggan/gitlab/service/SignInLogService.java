package com.kangyonggan.gitlab.service;

import com.kangyonggan.gitlab.dto.PageRequest;
import com.kangyonggan.gitlab.model.SignInLog;

import java.util.List;

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

    /**
     * 分页查询登录日志
     *
     * @param request
     * @param currentUserId
     * @return
     */
    List<SignInLog> searchSignInLog(PageRequest request, Long currentUserId);
}
