package com.kangyonggan.gitlab.service;

import com.kangyonggan.gitlab.dto.GroupRequest;
import com.kangyonggan.gitlab.model.Group;

import java.util.List;

/**
 * @author kyg
 */
public interface GroupService {

    /**
     * 搜索组
     *
     * @param request
     * @return
     */
    List<Group> searchGroups(GroupRequest request);

    /**
     * 保存组
     *
     * @param group
     * @param userId
     * @return
     */
    Group saveGroup(Group group, Long userId);

    /**
     * 查询组
     *
     * @param id
     * @return
     */
    Group getGroup(Long id);

    /**
     * 更新组
     *
     * @param group
     */
    void updateGroup(Group group);

    /**
     * 删除组
     *
     * @param id
     */
    void removeGroup(Long id);

    /**
     * 判断组路径是否存在
     *
     * @param groupPath
     * @return
     */
    boolean existsGroupPath(String groupPath);
}
