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
     */
    void saveGroup(Group group, Long userId);

    /**
     * 更新组
     *
     * @param group
     * @throws Exception
     */
    void updateGroup(Group group) throws Exception;

    /**
     * 删除组
     *
     * @param id
     * @throws Exception
     */
    void removeGroup(Long id) throws Exception;

    /**
     * 判断组路径是否存在
     *
     * @param groupPath
     * @return
     */
    boolean existsGroupPath(String groupPath);

    /**
     * 查询组
     *
     * @param groupPath
     * @return
     */
    Group findGroupByPath(String groupPath);

    /**
     * 删除自己是唯一owner的组
     *
     * @param userId
     */
    void removeOnlyOwnerGroups(Long userId);

    /**
     * 查询全部组
     *
     * @return
     */
    List<Group> findAllGroups();

    /**
     * 查询用户的全部组
     *
     * @param userId
     * @return
     */
    List<Group> findUserGroups(Long userId);
}
