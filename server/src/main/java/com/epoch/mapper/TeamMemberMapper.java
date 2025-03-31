package com.epoch.mapper;

import com.epoch.entity.TeamMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 团队成员Mapper接口
 */
@Mapper
public interface TeamMemberMapper {
    
    /**
     * 保存团队成员
     * @param teamMember 团队成员对象
     * @return 影响行数
     */
    int insert(TeamMember teamMember);
    
    /**
     * 批量保存团队成员
     * @param teamMembers 团队成员列表
     * @return 影响行数
     */
    int batchInsert(List<TeamMember> teamMembers);
    
    /**
     * 根据报名ID查询团队成员列表
     * @param registrationId 报名ID
     * @return 团队成员列表
     */
    List<TeamMember> listByRegistrationId(Long registrationId);
    
    /**
     * 根据成员用户ID查询团队成员列表
     * @param memberUserId 成员用户ID
     * @return 团队成员列表
     */
    List<TeamMember> listByMemberUserId(Long memberUserId);
    
    /**
     * 根据报名ID和成员用户ID查询团队成员
     * @param registrationId 报名ID
     * @param memberUserId 成员用户ID
     * @return 团队成员对象
     */
    TeamMember getByRegistrationIdAndMemberUserId(@Param("registrationId") Long registrationId, @Param("memberUserId") Long memberUserId);
    
    /**
     * 根据报名ID删除团队成员
     * @param registrationId 报名ID
     * @return 影响行数
     */
    int deleteByRegistrationId(Long registrationId);
    
    /**
     * 根据报名ID和成员用户ID删除团队成员
     * @param registrationId 报名ID
     * @param memberUserId 成员用户ID
     * @return 影响行数
     */
    int deleteByRegistrationIdAndMemberUserId(@Param("registrationId") Long registrationId, @Param("memberUserId") Long memberUserId);
} 