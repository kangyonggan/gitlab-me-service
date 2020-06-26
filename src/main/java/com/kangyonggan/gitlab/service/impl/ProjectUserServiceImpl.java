package com.kangyonggan.gitlab.service.impl;

import com.kangyonggan.gitlab.annotation.MethodLog;
import com.kangyonggan.gitlab.dto.ProjectUserDto;
import com.kangyonggan.gitlab.mapper.ProjectUserMapper;
import com.kangyonggan.gitlab.model.ProjectUser;
import com.kangyonggan.gitlab.service.BaseService;
import com.kangyonggan.gitlab.service.ProjectUserService;
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
}
