package com.kangyonggan.gitlab.service.impl;

import com.kangyonggan.gitlab.annotation.MethodLog;
import com.kangyonggan.gitlab.dto.PageRequest;
import com.kangyonggan.gitlab.model.SignInLog;
import com.kangyonggan.gitlab.service.BaseService;
import com.kangyonggan.gitlab.service.SignInLogService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

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

    @Override
    public List<SignInLog> searchSignInLog(PageRequest request, Long userId) {
        Example example = new Example(SignInLog.class);
        example.createCriteria().andEqualTo("userId", userId);

        sortAndPage(request, example);
        return baseMapper.selectByExample(example);
    }
}
