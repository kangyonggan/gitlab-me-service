package com.kangyonggan.gitlab.service.impl;

import com.kangyonggan.gitlab.annotation.MethodLog;
import com.kangyonggan.gitlab.constants.Access;
import com.kangyonggan.gitlab.dto.BlobInfo;
import com.kangyonggan.gitlab.dto.ProjectInfo;
import com.kangyonggan.gitlab.dto.ProjectRequest;
import com.kangyonggan.gitlab.dto.TreeInfo;
import com.kangyonggan.gitlab.model.Project;
import com.kangyonggan.gitlab.model.ProjectUser;
import com.kangyonggan.gitlab.service.BaseService;
import com.kangyonggan.gitlab.service.ProjectService;
import com.kangyonggan.gitlab.service.ProjectUserService;
import com.kangyonggan.gitlab.util.Encodes;
import com.kangyonggan.gitlab.util.ShellUtil;
import com.kangyonggan.gitlab.util.StringUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kyg
 */
@Service
@Log4j2
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
    public ProjectInfo findProjectInfo(String namespace, String projectPath, String branch) throws Exception {
        Project project = findProjectByNamespaceAndPath(namespace, projectPath);
        ProjectInfo projectInfo = new ProjectInfo();
        BeanUtils.copyProperties(project, projectInfo);

        // 大小
        // git count-objects -v
        String size = ShellUtil.exec("git --git-dir " + projectRoot + "/" + namespace + "/" + projectPath + ".git count-objects -v").get(1);
        projectInfo.setSize(Long.parseLong(size.trim().split("\\s")[1]));

        // 最后提交时间
        // git log master --date=raw -1
        List<String> list = ShellUtil.exec("git --git-dir " + projectRoot + "/" + namespace + "/" + projectPath + ".git log " + branch + " --date=raw -1");
        if (!list.isEmpty()) {
            Map<String, Object> lastCommit = new HashMap<>(8);
            lastCommit.put("commitId", list.get(0).trim().split("\\s+")[1]);
            lastCommit.put("username", list.get(1).trim().split("\\s+")[1]);
            lastCommit.put("email", list.get(1).trim().split("\\s+")[2].replace("<", "").replace(">", ""));
            lastCommit.put("date", list.get(2).trim().split("\\s+")[1] + "000");
            lastCommit.put("msg", list.get(4).trim());
            projectInfo.setLastCommit(lastCommit);
        }

        // 提交次数
        // git log master --oneline | wc -l
        String commitNums = ShellUtil.execSimple("git --git-dir " + projectRoot + "/" + namespace + "/" + projectPath + ".git log " + branch + " --oneline | wc -l");
        projectInfo.setCommitNums(Integer.parseInt(commitNums.trim()));

        // 分支
        // git branch
        List<String> branches = ShellUtil.exec("git --git-dir " + projectRoot + "/" + namespace + "/" + projectPath + ".git branch");
        projectInfo.setBranches(formatBranches(branches));

        // 标签
        // git tag
        List<String> tags = ShellUtil.exec("git --git-dir " + projectRoot + "/" + namespace + "/" + projectPath + ".git tag");
        projectInfo.setTags(tags);

        return projectInfo;
    }

    @Override
    @MethodLog
    public List<TreeInfo> getProjectTree(String namespace, String projectPath, String branch, String fullPath) throws Exception {
        List<TreeInfo> treeInfos = new ArrayList<>();
        Project project = findProjectByNamespaceAndPath(namespace, projectPath);
        // git ls-tree -l master HEAD service/2.txt
        List<String> list = ShellUtil.exec("git --git-dir " + projectRoot + "/" + project.getNamespace() + "/" + project.getProjectPath() + ".git ls-tree -l " + branch + " HEAD " + fullPath);
        for (String line : list) {
            // line look like: 100644 blob e69de29bb2d1d6434b8b29ae775ad8c2e48c5391       0	service/2.txt
            String[] arr = line.split("\\s+");
            TreeInfo treeInfo = new TreeInfo();
            treeInfo.setType(arr[1]);
            treeInfo.setIsh(arr[2]);
            if (!"-".equals(arr[3])) {
                treeInfo.setSize(Long.parseLong(arr[3]));
            }
            treeInfo.setFullName(Encodes.decodeOct(arr[4].replaceAll("\"", "")));

            // last commit
            // git log dev-kyg -1 -- service/2.txt
            List<String> lastCommit = ShellUtil.exec("git --git-dir " + projectRoot + "/" + project.getNamespace() + "/" + project.getProjectPath() + ".git log " + branch + " --date=raw -1 -- " + treeInfo.getFullName());
            if (!lastCommit.isEmpty()) {
                Map<String, Object> map = new HashMap<>(8);
                map.put("commitId", lastCommit.get(0).trim().split("\\s+")[1]);
                map.put("date", lastCommit.get(2).trim().split("\\s+")[1] + "000");
                map.put("msg", lastCommit.get(4).trim());
                treeInfo.setLastCommit(map);
            }

            treeInfos.add(treeInfo);
        }
        return treeInfos;
    }

    @Override
    @MethodLog
    public BlobInfo getProjectBlob(String namespace, String projectPath, String branch, String fullPath) throws Exception {
        BlobInfo blobInfo = new BlobInfo();
        blobInfo.setFullName(fullPath);
        Project project = findProjectByNamespaceAndPath(namespace, projectPath);

        // 文件属性
        String line = ShellUtil.execSimple("git --git-dir " + projectRoot + "/" + project.getNamespace() + "/" + project.getProjectPath() + ".git ls-tree -l " + branch + " HEAD " + fullPath);
        // line look like: 100644 blob e69de29bb2d1d6434b8b29ae775ad8c2e48c5391       0	service/2.txt
        String[] arr = line.split("\\s+");
        blobInfo.setIsh(arr[2]);
        blobInfo.setSize(Long.parseLong(arr[3]));

        // 文件内容
        String ext = FilenameUtils.getExtension(fullPath).toUpperCase();
        // 只读取常见类型的文件内容，并且文件要小于2M，否则就去下载吧
        if ("MD,TXT,XML,YML,PROPERTIES,GITIGNORE,SQL,SH,JAVA,JS,CSS,JSON,HTML".contains(ext)
                && blobInfo.getSize() < 2097152) {
            String content = ShellUtil.execSimple("git --git-dir " + projectRoot + "/" + project.getNamespace() + "/" + project.getProjectPath() + ".git show " + branch + ":" + fullPath);
            blobInfo.setContent(content);
        }

        // last commit
        blobInfo.setLastCommit(getLastCommit(namespace, projectPath, branch, fullPath));

        return blobInfo;
    }

    @Override
    @MethodLog
    public Map<String, Object> getLastCommit(String namespace, String projectPath, String branch, String fullPath) throws Exception {
        // git log dev-kyg --date=raw -1 -- service/2.txt
        List<String> lastCommit = ShellUtil.exec("git --git-dir " + projectRoot + "/" + namespace + "/" + projectPath + ".git log " + branch + " --date=raw -1 -- " + fullPath);
        if (!lastCommit.isEmpty()) {
            Map<String, Object> map = new HashMap<>(4);
            map.put("commitId", lastCommit.get(0).trim().split("\\s+")[1]);
            map.put("author", lastCommit.get(1).trim().split("\\s+")[1]);
            map.put("date", lastCommit.get(2).trim().split("\\s+")[1] + "000");
            map.put("msg", lastCommit.get(4).trim());

            return map;
        }
        return null;
    }

    @Override
    @MethodLog
    public void newBranch(String namespace, String projectPath, String branchName, String createFrom) throws Exception {
        String msg = ShellUtil.execSimple("git --git-dir " + projectRoot + "/" + namespace + "/" + projectPath + ".git branch " + branchName + " " + createFrom);
        if (StringUtils.isNotEmpty(msg)) {
            throw new RuntimeException(msg);
        }
    }

    private List<String> formatBranches(List<String> branches) {
        for (int i = 0; i < branches.size(); i++) {
            String branch = branches.get(i).trim();
            if (branch.startsWith("*")) {
                branch = branch.substring(1).trim();
            }
            branches.set(i, branch);
        }
        return branches;
    }

}
