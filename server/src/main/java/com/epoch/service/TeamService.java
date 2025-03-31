package com.epoch.service;

import com.epoch.dto.TeamCreateDTO;
import com.epoch.dto.TeamMemberAddDTO;
import com.epoch.dto.TeamQueryDTO;
import com.epoch.dto.TeamUpdateDTO;
import com.epoch.vo.TeamDetailVO;
import com.epoch.vo.TeamPageVO;

/**
 * 团队管理Service接口
 */
public interface TeamService {
    
    /**
     * 创建团队
     * @param userId 用户ID
     * @param dto 创建参数
     * @return 团队ID
     */
    Long createTeam(Long userId, TeamCreateDTO dto);
    
    /**
     * 更新团队信息
     * @param teamId 团队ID
     * @param dto 更新参数
     */
    void updateTeam(Long teamId, TeamUpdateDTO dto);
    
    /**
     * 获取团队详情
     * @param teamId 团队ID
     * @return 团队详情
     */
    TeamDetailVO getTeamDetail(Long teamId);
    
    /**
     * 获取团队列表
     * @param dto 查询参数
     * @return 团队分页列表
     */
    TeamPageVO listTeams(TeamQueryDTO dto);
    
    /**
     * 删除团队
     * @param teamId 团队ID
     */
    void deleteTeam(Long teamId);
    
    /**
     * 添加团队成员
     * @param teamId 团队ID
     * @param dto 添加参数
     */
    void addTeamMembers(Long teamId, TeamMemberAddDTO dto);
    
    /**
     * 移除团队成员
     * @param teamId 团队ID
     * @param memberId 成员ID
     */
    void removeTeamMember(Long teamId, Long memberId);
} 