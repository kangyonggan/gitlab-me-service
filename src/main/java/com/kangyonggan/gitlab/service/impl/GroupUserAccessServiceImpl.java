package com.kangyonggan.gitlab.service.impl;

import com.kangyonggan.gitlab.annotation.MethodLog;
import com.kangyonggan.gitlab.model.Group;
import com.kangyonggan.gitlab.model.GroupUserAccess;
import com.kangyonggan.gitlab.service.BaseService;
import com.kangyonggan.gitlab.service.GroupUserAccessService;
import org.springframework.stereotype.Service;

/**
 * @author kyg
 */
@Service
public class GroupUserAccessServiceImpl extends BaseService<GroupUserAccess> implements GroupUserAccessService {

    @Override
    @MethodLog
    public void saveGroupUserAccess(GroupUserAccess access) {
        baseMapper.insertSelective(access);
    }

    @Override
    public void removeGroupUserAccess(Long groupId) {
        GroupUserAccess access = new GroupUserAccess();
        access.setGroupId(groupId);

        baseMapper.delete(access);
    }
}
