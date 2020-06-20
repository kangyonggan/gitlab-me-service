package com.kangyonggan.gitlab.mapper;

import com.kangyonggan.gitlab.extra.BaseMapper;
import com.kangyonggan.gitlab.model.Group;
import org.apache.ibatis.annotations.Param;

/**
 * @author mbg
 */
public interface GroupMapper extends BaseMapper<Group> {

    /**
     * 删除自己是唯一owner的组
     *
     * @param userId
     */
    void deleteOnlyOwnerGroups(@Param("userId") Long userId);
}