package com.kangyonggan.gitlab.service.impl;

import com.kangyonggan.gitlab.annotation.MethodLog;
import com.kangyonggan.gitlab.dto.ProjectRequest;
import com.kangyonggan.gitlab.model.Project;
import com.kangyonggan.gitlab.service.BaseService;
import com.kangyonggan.gitlab.service.ProjectService;
import com.kangyonggan.gitlab.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author kyg
 */
@Service
public class ProjectServiceImpl extends BaseService<Project> implements ProjectService {

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

        sortAndPage(request, example);
        return baseMapper.selectByExample(example);
    }

    @Override
    @MethodLog
    @Transactional(rollbackFor = Exception.class)
    public void saveProject(Project project, Long currentUserId) {
        baseMapper.insertSelective(project);
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
    public void removeProject(Long id) {
        baseMapper.deleteByPrimaryKey(id);
    }
}
