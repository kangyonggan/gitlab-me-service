package com.kangyonggan.gitlab.mapper;

import com.kangyonggan.gitlab.extra.BaseMapper;
import com.kangyonggan.gitlab.model.User;

import java.util.List;

/**
 * @author mbg
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 查询除了指定组的用户
     *
     * @param groupId
     * @return
     */
    List<User> selectUsersWithoutGroup(Long groupId);

}