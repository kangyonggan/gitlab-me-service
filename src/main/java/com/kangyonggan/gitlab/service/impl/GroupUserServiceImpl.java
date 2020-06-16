package com.kangyonggan.gitlab.service.impl;

import com.kangyonggan.gitlab.annotation.MethodLog;
import com.kangyonggan.gitlab.dto.GroupUserDto;
import com.kangyonggan.gitlab.mapper.GroupUserMapper;
import com.kangyonggan.gitlab.model.GroupUser;
import com.kangyonggan.gitlab.service.BaseService;
import com.kangyonggan.gitlab.service.GroupUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author kyg
 */
@Service
public class GroupUserServiceImpl extends BaseService<GroupUser> implements GroupUserService {

    @Autowired
    private GroupUserMapper groupUserMapper;

    @Override
    @MethodLog
    public void saveGroupUser(GroupUser groupUser) {
        baseMapper.insertSelective(groupUser);
    }

    @Override
    public void removeGroupUser(Long groupId) {
        GroupUser access = new GroupUser();
        access.setGroupId(groupId);

        baseMapper.delete(access);
    }

    @Override
    public void saveGroupUsers(Long groupId, byte access, Long[] userIds) {
        groupUserMapper.insertGroupUsers(groupId, access, userIds);
    }

    @Override
    @MethodLog
    public List<GroupUserDto> findGroupUsers(Long groupId) {
        return groupUserMapper.selectGroupUsers(groupId);
    }
}
