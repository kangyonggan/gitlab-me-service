package com.kangyonggan.gitlab.service.impl;

import com.kangyonggan.gitlab.annotation.MethodLog;
import com.kangyonggan.gitlab.constants.Access;
import com.kangyonggan.gitlab.dto.GroupRequest;
import com.kangyonggan.gitlab.mapper.GroupMapper;
import com.kangyonggan.gitlab.model.Group;
import com.kangyonggan.gitlab.model.GroupUser;
import com.kangyonggan.gitlab.service.BaseService;
import com.kangyonggan.gitlab.service.GroupService;
import com.kangyonggan.gitlab.service.GroupUserService;
import com.kangyonggan.gitlab.service.ProjectService;
import com.kangyonggan.gitlab.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author kyg
 */
@Service
public class GroupServiceImpl extends BaseService<Group> implements GroupService {

    @Autowired
    private GroupUserService groupUserService;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private ProjectService projectService;

    @Override
    public List<Group> searchGroups(GroupRequest request) {
        Example example = new Example(Group.class);
        Example.Criteria criteria = example.createCriteria();

        String groupPath = request.getGroupPath();
        if (StringUtils.isNotEmpty(groupPath)) {
            criteria.andLike("groupPath", StringUtil.toLike(groupPath));
        }
        String groupName = request.getGroupName();
        if (StringUtils.isNotEmpty(groupName)) {
            criteria.andLike("groupName", StringUtil.toLike(groupName));
        }

        sortAndPage(request, example);
        return baseMapper.selectByExample(example);
    }

    @Override
    @MethodLog
    @Transactional(rollbackFor = Exception.class)
    public void saveGroup(Group group, Long userId) {
        baseMapper.insertSelective(group);

        GroupUser groupUser = new GroupUser();
        groupUser.setGroupId(group.getId());
        groupUser.setUserId(userId);
        groupUser.setAccess(Access.Owner.getCode());

        groupUserService.saveGroupUser(groupUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateGroup(Group group) throws Exception {
        Group oldGroup = baseMapper.selectByPrimaryKey(group.getId());
        if (StringUtils.isNotEmpty(group.getGroupPath()) && !oldGroup.getGroupPath().equals(group.getGroupPath())) {
            // 更新项目的命名空间
            projectService.updateProjectNamespace(oldGroup.getGroupPath(), group.getGroupPath());
        }
        baseMapper.updateByPrimaryKeySelective(group);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeGroup(Long id) throws Exception {
        Group group = baseMapper.selectByPrimaryKey(id);

        baseMapper.deleteByPrimaryKey(id);

        // 删除组用户
        groupUserService.removeGroupUsers(id);

        // 项目用户的项目
        projectService.removeProjectByNamespace(group.getGroupPath());
    }

    @Override
    @MethodLog
    public boolean existsGroupPath(String groupPath) {
        Group group = new Group();
        group.setGroupPath(groupPath);
        return baseMapper.selectCount(group) > 0;
    }

    @Override
    @MethodLog
    public Group findGroupByPath(String groupPath) {
        Group group = new Group();
        group.setGroupPath(groupPath);
        return baseMapper.selectOne(group);
    }

    @Override
    @MethodLog
    public void removeOnlyOwnerGroups(Long userId) {
        groupMapper.deleteOnlyOwnerGroups(userId);
    }

    @Override
    public List<Group> findAllGroups() {
        return baseMapper.selectAll();
    }

    @Override
    public List<Group> findUserGroups(Long userId) {
        return groupMapper.selectUserGroups(userId);
    }
}
