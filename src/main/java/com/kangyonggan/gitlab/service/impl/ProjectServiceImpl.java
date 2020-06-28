package com.kangyonggan.gitlab.service.impl;

import com.kangyonggan.gitlab.annotation.MethodLog;
import com.kangyonggan.gitlab.constants.Access;
import com.kangyonggan.gitlab.dto.ProjectInfo;
import com.kangyonggan.gitlab.dto.ProjectRequest;
import com.kangyonggan.gitlab.model.Project;
import com.kangyonggan.gitlab.model.ProjectUser;
import com.kangyonggan.gitlab.service.BaseService;
import com.kangyonggan.gitlab.service.ProjectService;
import com.kangyonggan.gitlab.service.ProjectUserService;
import com.kangyonggan.gitlab.util.ShellUtil;
import com.kangyonggan.gitlab.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

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

    @Override
    @MethodLog
    @Transactional(rollbackFor = Exception.class)
    public void updateProjectNamespace(String oldNamespace, String namespace) throws Exception {
        Example example = new Example(Project.class);
        example.createCriteria().andEqualTo("namespace", oldNamespace);

        Project project = new Project();
        project.setNamespace(namespace);
        baseMapper.updateByExampleSelective(project, example);

        // 转移命名空间
        ShellUtil.exec("sh " + binPath + "/move_namespace.sh " + projectRoot + " " + oldNamespace + " " + namespace);
    }

    @Override
    @MethodLog
    @Transactional(rollbackFor = Exception.class)
    public void removeProjectByNamespace(String namespace) throws Exception {
        projectUserService.removeProjectUsers(namespace);

        Project project = new Project();
        project.setNamespace(namespace);

        baseMapper.delete(project);

        // 删除命名空间下的项目
        ShellUtil.exec("sh " + binPath + "/del_project.sh " + projectRoot + " " + namespace);
    }

    @Override
    @MethodLog
    public ProjectInfo findProjectInfo(String namespace, String projectPath) throws Exception {
        Project project = findProjectByNamespaceAndPath(namespace, projectPath);
        ProjectInfo projectInfo = new ProjectInfo();
        BeanUtils.copyProperties(project, projectInfo);

        String result = ShellUtil.execSimple("du -sh " + projectRoot + "/" + namespace + "/" + projectPath + ".git/objects");
        projectInfo.setSize(result.trim().split("\\s")[0]);

        String ref = ShellUtil.execSimple("cat " + projectRoot + "/" + namespace + "/" + projectPath + ".git/refs/heads/master");
        if (StringUtils.isNotEmpty(ref)) {
            List<String> list = ShellUtil.exec("git --git-dir " + projectRoot + "/" + namespace + "/" + projectPath + ".git log " + ref);
            if (!list.isEmpty()) {
                String dateStr = list.get(2).substring(6).trim();
                dateStr = dateStr.substring(dateStr.indexOf(" ")).trim();
                projectInfo.setLastCommitTime(new SimpleDateFormat("MMM d HH:mm:ss yyyy", Locale.ENGLISH).parse(dateStr));
            }
        }

        return projectInfo;
    }

}
