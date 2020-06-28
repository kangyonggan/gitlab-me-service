package com.kangyonggan.gitlab.controller.projects;

import com.kangyonggan.gitlab.controller.BaseController;
import com.kangyonggan.gitlab.dto.ProjectInfo;
import com.kangyonggan.gitlab.dto.Response;
import com.kangyonggan.gitlab.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kyg
 */
@RestController
@RequestMapping("projects")
public class ProjectsDetailsController extends BaseController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("{namespace:[\\w]+}/{projectPath:[\\w]+}")
    public Response details(@PathVariable String namespace, @PathVariable String projectPath) throws Exception {
        Response response = successResponse();
        ProjectInfo project = projectService.findProjectInfo(namespace, projectPath);

        response.put("project", project);
        return response;
    }

}
