package com.kangyonggan.gitlab;

import com.kangyonggan.gitlab.model.Project;
import com.kangyonggan.gitlab.service.ProjectService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author kyg
 */
public class ProjectServiceTest extends AbstractTest {

    @Autowired
    private ProjectService projectService;

    @Test
    public void save() throws Exception {
        Project project = new Project();
        project.setNamespace("fes");
        project.setProjectPath("junit-" + (System.currentTimeMillis() % 1000));
        project.setProjectName("junit项目");
        project.setDescription("随便描述");
        project.setVisibilityLevel((byte) 0);

        projectService.saveProject(project, 1L);
    }

}
