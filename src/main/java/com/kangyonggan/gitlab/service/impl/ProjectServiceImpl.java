package com.kangyonggan.gitlab.service.impl;

import com.kangyonggan.gitlab.annotation.MethodLog;
import com.kangyonggan.gitlab.constants.Access;
import com.kangyonggan.gitlab.dto.ProjectRequest;
import com.kangyonggan.gitlab.model.Project;
import com.kangyonggan.gitlab.model.ProjectUser;
import com.kangyonggan.gitlab.service.BaseService;
import com.kangyonggan.gitlab.service.ProjectService;
import com.kangyonggan.gitlab.service.ProjectUserService;
import com.kangyonggan.gitlab.util.ShellUtil;
import com.kangyonggan.gitlab.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${gitlab.bin-path}")
    private String binPath;

    @Value("${gitlab.project-root}")
    private String projectRoot;

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
    public void saveProject(Project project, Long userId) throws Exception {
        baseMapper.insertSelective(project);

        ProjectUser projectUser = new ProjectUser();
        projectUser.setProjectId(project.getId());
        projectUser.setUserId(userId);
        projectUser.setAccess(Access.Owner.getCode());

        projectUserService.saveProjectUser(projectUser);

        // 创建项目
        ShellUtil.exec("sh " + binPath + "/create_project.sh " + projectRoot + " " + project.getNamespace() + " " + project.getProjectPath());
    }

    @Override
    @MethodLog
    public Project findProjectByNamespaceAndPath(String namespace, String projectPath) {
        Project project = new Project();
        project.setNamespace(namespace);
        project.setProjectPath(projectPath);

        return baseMapper.selectOne(project);
    }

    @Override
    @MethodLog
    @Transactional(rollbackFor = Exception.class)
    public void updateProject(Project project) throws Exception {
        Project oldProject = baseMapper.selectByPrimaryKey(project.getId());
        baseMapper.updateByPrimaryKeySelective(project);

        boolean hasChange = (StringUtils.isNotEmpty(project.getProjectPath()) && !oldProject.getProjectPath().equals(project.getProjectPath()))
                || (StringUtils.isNotEmpty(project.getNamespace()) && !oldProject.getNamespace().equals(project.getNamespace()));
        if (hasChange) {
            String newNamespace = project.getNamespace();
            String newProjectPath = project.getProjectPath();
            if (StringUtils.isEmpty(newNamespace)) {
                newNamespace = oldProject.getNamespace();
            }
            if (StringUtils.isEmpty(newProjectPath)) {
                newProjectPath = oldProject.getProjectPath();
            }

            // 移动项目
            ShellUtil.exec("sh " + binPath + "/move_project.sh " + projectRoot + " " + oldProject.getNamespace() + " " + oldProject.getProjectPath()
                    + " " + newNamespace + " " + newProjectPath);
        }
    }

    @Override
    @MethodLog
    @Transactional(rollbackFor = Exception.class)
    public void removeProject(Long id) throws Exception {
        Project project = baseMapper.selectByPrimaryKey(id);
        baseMapper.deleteByPrimaryKey(id);

        // 删除项目用户
        projectUserService.removeProjectUsers(id);

        // 删除项目
        ShellUtil.exec("sh " + binPath + "/del_project.sh " + projectRoot + " " + project.getNamespace() + " " + project.getProjectPath());

    }

    @Override
    @MethodLog
    public boolean existsProjectPath(String namespace, String projectPath) {
        Project project = new Project();
        project.setNamespace(namespace);
        project.setProjectPath(projectPath);
        return baseMapper.selectCount(project) > 0;
    }
}
