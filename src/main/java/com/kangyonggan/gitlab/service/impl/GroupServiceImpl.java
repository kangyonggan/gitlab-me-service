package com.kangyonggan.gitlab.service.impl;

import com.kangyonggan.gitlab.annotation.MethodLog;
import com.kangyonggan.gitlab.constants.Access;
import com.kangyonggan.gitlab.model.Group;
import com.kangyonggan.gitlab.model.GroupUserAccess;
import com.kangyonggan.gitlab.service.BaseService;
import com.kangyonggan.gitlab.service.GroupService;
import com.kangyonggan.gitlab.service.GroupUserAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author kyg
 */
@Service
public class GroupServiceImpl extends BaseService<Group> implements GroupService {

    @Autowired
    private GroupUserAccessService groupUserAccessService;

    @Override
    @MethodLog
    @Transactional(rollbackFor = Exception.class)
    public Group saveGroup(Group group, Long userId) {
        baseMapper.insertSelective(group);

        GroupUserAccess access = new GroupUserAccess();
        access.setGroupId(group.getId());
        access.setUserId(userId);
        access.setAccess(Access.Owner.name());

        groupUserAccessService.saveGroupUserAccess(access);

        return group;
    }

    @Override
    public Group getGroup(Long id) {
        return baseMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateGroup(Group group) {
        baseMapper.updateByPrimaryKeySelective(group);

        // TODO group path 修改，组项目空间也要更新
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeGroup(Long id) {
        baseMapper.deleteByPrimaryKey(id);

        // 删除组用户权限
        groupUserAccessService.removeGroupUserAccess(id);
    }

    @Override
    @MethodLog
    public boolean existsGroupPath(String groupPath) {
        Group group = new Group();
        group.setGroupPath(groupPath);
        return baseMapper.selectCount(group) > 0;
    }
}
