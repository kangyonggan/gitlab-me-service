package com.kangyonggan.gitlab.controller;

import com.kangyonggan.gitlab.constants.Access;
import com.kangyonggan.gitlab.service.GroupUserService;
import com.kangyonggan.gitlab.service.ProjectUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kyg
 */
@RestController
@RequestMapping("access")
public class AccessController {

    @Autowired
    private GroupUserService groupUserService;

    @Autowired
    private ProjectUserService projectUserService;

    /**
     * 检查访问权限。
     *
     * @param namespace
     * @param projectPath
     * @param username
     * @param branchName
     * @return 0：允许访问，非0：不允许访问
     */
    @GetMapping
    public Integer checkAccess(@RequestParam String namespace, @RequestParam String projectPath,
                               @RequestParam String username, @RequestParam String branchName) {
        if (!"master".equals(branchName)) {
            return 0;
        }
        // 项目权限优先
        Byte access = projectUserService.findProjectAccess(namespace, projectPath, username);
        if (access == null || access != Access.Owner.getCode()) {
            access = groupUserService.findGroupAccess(namespace, username);
        }

        return access == null || access != Access.Owner.getCode() ? -1 : 0;
    }

}
