package com.kangyonggan.gitlab.service.impl;

import com.kangyonggan.gitlab.annotation.MethodLog;
import com.kangyonggan.gitlab.constants.Access;
import com.kangyonggan.gitlab.dto.ProjectRequest;
import com.kangyonggan.gitlab.model.Project;
import com.kangyonggan.gitlab.model.ProjectUser;
import com.kangyonggan.gitlab.service.BaseService;
import com.kangyonggan.gitlab.service.ProjectService;
import com.kangyonggan.gitlab.service.ProjectUserService;
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
public class ProjectServiceImpl extends BaseService<Project> implements ProjectService {

    @Autowired
    private ProjectUserService projectUserService;

    @Override
    public List<Project> searchProjects(ProjectRequest request) {
        Example example = new Example(Project.class);
        Example.Criteria criteria = example.createCriteria();

        String projectPath = request.getProjectPath();
        if (StringUtils.isNotEmpty(projectPath)) {
            criteria.andLike("projectPath", StringUtil.toLike(projectPath));
        }
        String projectName = request.getProjectName();
        if (StringUtils.isNotEmpty(projectName)) {
            criteria.andLike("projectName", StringUtil.toLike(projectName));
        }
        String namespace = request.getNamespace();
        if (StringUtils.isNotEmpty(namespace)) {
            criteria.andEqualTo("namespace", namespace);
        }

        sortAndPage(request, example);
        return baseMapper.selectByExample(example);
    }

    @Override
    @MethodLog
    @Transactional(rollbackFor = Exception.class)
    public void saveProject(Project project, Long userId) {
        baseMapper.insertSelective(project);

        ProjectUser projectUser = new ProjectUser();
        projectUser.setProjectId(project.getId());
        projectUser.setUserId(userId);
        projectUser.setAccess(Access.Owner.getCode());

        projectUserService.saveProjectUser(projectUser);
    }

    @Override
    @MethodLog
    public Project findProjectByPath(String projectPath) {
        Project project = new Project();
        project.setProjectPath(projectPath);

        return baseMapper.selectOne(project);
    }

    @Override
    @MethodLog
    public void updateProject(Project project) {
        baseMapper.updateByPrimaryKeySelective(project);
    }

    @Override
    @MethodLog
    @Transactional(rollbackFor = Exception.class)
    public void removeProject(Long id) {
        baseMapper.deleteByPrimaryKey(id);

        // 删除项目用户
        projectUserService.removeProjectUsers(id);
    }
}
