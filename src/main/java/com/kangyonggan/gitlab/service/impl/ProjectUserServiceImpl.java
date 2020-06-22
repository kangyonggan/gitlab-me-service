package com.kangyonggan.gitlab.service.impl;

import com.kangyonggan.gitlab.annotation.MethodLog;
import com.kangyonggan.gitlab.model.ProjectUser;
import com.kangyonggan.gitlab.service.BaseService;
import com.kangyonggan.gitlab.service.ProjectUserService;
import org.springframework.stereotype.Service;

/**
 * @author kyg
 */
@Service
public class ProjectUserServiceImpl extends BaseService<ProjectUser> implements ProjectUserService {

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
}
