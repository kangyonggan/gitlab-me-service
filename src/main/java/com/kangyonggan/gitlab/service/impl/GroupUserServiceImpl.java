package com.kangyonggan.gitlab.service.impl;

import com.kangyonggan.gitlab.annotation.MethodLog;
import com.kangyonggan.gitlab.constants.Access;
import com.kangyonggan.gitlab.dto.GroupUserDto;
import com.kangyonggan.gitlab.mapper.GroupUserMapper;
import com.kangyonggan.gitlab.model.GroupUser;
import com.kangyonggan.gitlab.service.BaseService;
import com.kangyonggan.gitlab.service.GroupUserService;
import com.kangyonggan.gitlab.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
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
    public void removeGroupUsers(Long groupId) {
        GroupUser access = new GroupUser();
        access.setGroupId(groupId);

        baseMapper.delete(access);
    }

    @Override
    public void saveGroupUsers(Long groupId, byte access, Long[] userIds, String expirationDate) {
        groupUserMapper.insertGroupUsers(groupId, access, userIds, expirationDate);
    }

    @Override
    @MethodLog
    public List<GroupUserDto> findGroupUsers(Long groupId) {
        return groupUserMapper.selectGroupUsers(groupId);
    }

    @Override
    @MethodLog
    public boolean removeGroupUser(Long groupId, Long groupUserId) {
        GroupUser groupUser = baseMapper.selectByPrimaryKey(groupUserId);
        if (Access.Owner.getCode() != groupUser.getAccess()) {
            baseMapper.deleteByPrimaryKey(groupUserId);
            return true;
        }

        groupUser = new GroupUser();
        groupUser.setGroupId(groupId);
        groupUser.setAccess(Access.Owner.getCode());

        if (baseMapper.selectCount(groupUser) != 1) {
            baseMapper.deleteByPrimaryKey(groupUserId);
            return true;
        }

        return false;
    }

    @Override
    @MethodLog
    public boolean updateGroupUser(Long groupId, Long groupUserId, byte access, String expirationDate) throws Exception {
        GroupUser groupUser = baseMapper.selectByPrimaryKey(groupUserId);
        if (access == Access.Owner.getCode() || Access.Owner.getCode() != groupUser.getAccess()) {
            groupUser.setAccess(access);
            if (StringUtils.isNotEmpty(expirationDate)) {
                groupUser.setExpirationDate(DateUtil.parseDate(expirationDate));
            }
            baseMapper.updateByPrimaryKeySelective(groupUser);
            return true;
        }

        groupUser = new GroupUser();
        groupUser.setGroupId(groupId);
        groupUser.setAccess(Access.Owner.getCode());

        if (baseMapper.selectCount(groupUser) != 1) {
            groupUser.setId(groupUserId);
            groupUser.setAccess(access);
            if (StringUtils.isNotEmpty(expirationDate)) {
                groupUser.setExpirationDate(DateUtil.parseDate(expirationDate));
            }
            baseMapper.updateByPrimaryKeySelective(groupUser);
            return true;
        }

        return false;
    }

    @Override
    @MethodLog
    public void removeUserGroups(Long userId) {
        GroupUser groupUser = new GroupUser();
        groupUser.setUserId(userId);
        baseMapper.delete(groupUser);
    }
}
