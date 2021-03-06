package com.kangyonggan.gitlab.mapper;

import com.kangyonggan.gitlab.dto.GroupUserDto;
import com.kangyonggan.gitlab.extra.BaseMapper;
import com.kangyonggan.gitlab.model.GroupUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author mbg
 */
public interface GroupUserMapper extends BaseMapper<GroupUser> {
    /**
     * 批量保存组成员
     *
     * @param groupId
     * @param access
     * @param userIds
     * @param expirationDate
     */
    void insertGroupUsers(@Param("groupId") Long groupId, @Param("access") byte access,
                          @Param("userIds") Long[] userIds, @Param("expirationDate") String expirationDate);

    /**
     * 查询组成员
     *
     * @param groupId
     * @return
     */
    List<GroupUserDto> selectGroupUsers(Long groupId);

    /**
     * 查询组访问权限
     *
     * @param groupPath
     * @param username
     * @return
     */
    Byte selectGroupAccess(@Param("groupPath") String groupPath, @Param("username") String username);
}