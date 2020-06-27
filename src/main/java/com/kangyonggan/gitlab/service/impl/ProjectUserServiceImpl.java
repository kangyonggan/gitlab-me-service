package com.kangyonggan.gitlab.service.impl;

import com.kangyonggan.gitlab.annotation.MethodLog;
import com.kangyonggan.gitlab.constants.Access;
import com.kangyonggan.gitlab.dto.ProjectUserDto;
import com.kangyonggan.gitlab.mapper.ProjectUserMapper;
import com.kangyonggan.gitlab.model.ProjectUser;
import com.kangyonggan.gitlab.service.BaseService;
import com.kangyonggan.gitlab.service.ProjectUserService;
import com.kangyonggan.gitlab.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author kyg
 */
@Service
public class ProjectUserServiceImpl extends BaseService<ProjectUser> implements ProjectUserService {

    @Autowired
    private ProjectUserMapper projectUserMapper;

    @Override
    @MethodLog
    public void saveProjectUser(ProjectUser projectUser) {
        baseMapper.insertSelective(projectUser);
    }

    @Override
    public void removeProjectUsers(Long projectId) {
        ProjectUser projectUser = new ProjectUser();
        projectUser.setProjectId(projectId);
        baseMapper.delete(projectUser);
    }

    @Override
    @MethodLog
    public Byte findProjectAccess(String namespace, String projectPath, String username) {
        return projectUserMapper.selectProjectAccess(namespace, projectPath, username);
    }

    @Override
    @MethodLog
    public List<ProjectUserDto> findProjectUsers(Long projectId) {
        return projectUserMapper.selectProjectUsers(projectId);
    }

    @Override
    @MethodLog
    public void removeProjectUsers(String namespace) {
        projectUserMapper.deleteProjectUsers(namespace);
    }

    @Override
    @MethodLog
    public boolean removeProjectUser(Long projectId, Long projectUserId) {
        ProjectUser projectUser = baseMapper.selectByPrimaryKey(projectUserId);
        if (Access.Owner.getCode() != projectUser.getAccess()) {
            baseMapper.deleteByPrimaryKey(projectUserId);
            return true;
        }

        projectUser = new ProjectUser();
        projectUser.setProjectId(projectId);
        projectUser.setAccess(Access.Owner.getCode());

        if (baseMapper.selectCount(projectUser) != 1) {
            baseMapper.deleteByPrimaryKey(projectUserId);
            return true;
        }

        return false;
    }

    @Override
    @MethodLog
    public boolean updateProjectUser(Long projectId, Long projectUserId, byte access, String expirationDate) throws Exception {
        ProjectUser projectUser = baseMapper.selectByPrimaryKey(projectUserId);
        if (access == Access.Owner.getCode() || Access.Owner.getCode() != projectUser.getAccess()) {
            projectUser.setAccess(access);
            if (StringUtils.isNotEmpty(expirationDate)) {
                projectUser.setExpirationDate(DateUtil.parseDate(expirationDate));
            }
            baseMapper.updateByPrimaryKeySelective(projectUser);
            return true;
        }

        projectUser = new ProjectUser();
        projectUser.setProjectId(projectId);
        projectUser.setAccess(Access.Owner.getCode());

        if (baseMapper.selectCount(projectUser) != 1) {
            projectUser.setId(projectUserId);
            projectUser.setAccess(access);
            if (StringUtils.isNotEmpty(expirationDate)) {
                projectUser.setExpirationDate(DateUtil.parseDate(expirationDate));
            }
            baseMapper.updateByPrimaryKeySelective(projectUser);
            return true;
        }

        return false;
    }

    @Override
    @MethodLog
    public void saveProjectUsers(Long projectId, byte access, Long[] userIds, String expirationDate) {
        projectUserMapper.insertProjectUsers(projectId, access, userIds, expirationDate);
    }
}
