package com.epoch.service.impl;

import com.epoch.constant.MessageConstant;
import com.epoch.dto.TeamCreateDTO;
import com.epoch.dto.TeamMemberAddDTO;
import com.epoch.dto.TeamQueryDTO;
import com.epoch.dto.TeamUpdateDTO;
import com.epoch.entity.Contest;
import com.epoch.entity.Registration;
import com.epoch.entity.TeamMember;
import com.epoch.entity.User;
import com.epoch.exception.BusinessException;
import com.epoch.mapper.ContestMapper;
import com.epoch.mapper.RegistrationMapper;
import com.epoch.mapper.TeamMemberMapper;
import com.epoch.mapper.UserMapper;
import com.epoch.service.TeamService;
import com.epoch.vo.TeamDetailVO;
import com.epoch.vo.TeamPageVO;
import com.epoch.vo.TeamVO;
import com.epoch.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 团队管理Service实现类
 */
@Service
@Slf4j
public class TeamServiceImpl implements TeamService {
    
    @Autowired
    private RegistrationMapper registrationMapper;
    
    @Autowired
    private TeamMemberMapper teamMemberMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private ContestMapper contestMapper;
    
    /**
     * 创建团队
     * @param userId 用户ID
     * @param dto 创建参数
     * @return 团队ID
     */
    @Override
    @Transactional
    public Long createTeam(Long userId, TeamCreateDTO dto) {
        // 检查竞赛是否存在
        Contest contest = contestMapper.getById(dto.getContestId());
        if (contest == null) {
            throw new BusinessException("竞赛不存在");
        }
        
        // 检查用户是否已经报名该竞赛
        List<Registration> existingRegistrations = registrationMapper.getByContestIdAndUserId(dto.getContestId(), userId);
        if (existingRegistrations != null && !existingRegistrations.isEmpty()) {
            throw new BusinessException("您已经报名该竞赛");
        }
        
        // 创建团队报名记录
        Registration registration = new Registration();
        registration.setContestId(dto.getContestId());
        registration.setUserId(userId);
        registration.setIsTeam(true);
        registration.setTeamName(dto.getTeamName());
        registration.setTeamDescription(dto.getTeamDescription());
        registration.setMaxMembers(dto.getMaxMembers() != null ? dto.getMaxMembers() : 5);
        registration.setStatus("pending");
        
        registrationMapper.insert(registration);
        
        // 添加团队成员
        if (dto.getMemberIds() != null && !dto.getMemberIds().isEmpty()) {
            // 检查成员数量是否超过限制
            if (dto.getMemberIds().size() > registration.getMaxMembers() - 1) {
                throw new BusinessException("成员数量超过限制");
            }
            
            List<TeamMember> teamMembers = new ArrayList<>();
            for (Long memberId : dto.getMemberIds()) {
                // 检查成员是否存在
                User member = userMapper.getById(memberId);
                if (member == null) {
                    throw new BusinessException("成员不存在: " + memberId);
                }
                
                // 检查成员是否已经报名该竞赛
                List<Registration> memberRegistrations = registrationMapper.getByContestIdAndUserId(dto.getContestId(), memberId);
                if (memberRegistrations != null && !memberRegistrations.isEmpty()) {
                    throw new BusinessException("成员已经报名该竞赛: " + member.getUsername());
                }
                
                TeamMember teamMember = new TeamMember();
                teamMember.setRegistrationId(registration.getId());
                teamMember.setMemberUserId(memberId);
                teamMembers.add(teamMember);
            }
            
            if (!teamMembers.isEmpty()) {
                teamMemberMapper.batchInsert(teamMembers);
            }
        }
        
        return registration.getId();
    }
    
    /**
     * 更新团队信息
     * @param teamId 团队ID
     * @param dto 更新参数
     */
    @Override
    @Transactional
    public void updateTeam(Long teamId, TeamUpdateDTO dto) {
        // 检查团队是否存在
        Registration registration = registrationMapper.getById(teamId);
        if (registration == null) {
            throw new BusinessException("团队不存在");
        }
        
       // 只有待审核状态的团队才能修改
//        if (!"pending".equals(registration.getStatus())) {
//            throw new BusinessException("只有待审核状态的团队才能修改");
//        }
        
        // 更新团队信息
        Registration updateReg = new Registration();
        updateReg.setId(teamId);
        
        if (StringUtils.hasText(dto.getTeamName())) {
            updateReg.setTeamName(dto.getTeamName());
        }
        
        if (StringUtils.hasText(dto.getTeamDescription())) {
            updateReg.setTeamDescription(dto.getTeamDescription());
        }
        
        if (dto.getMaxMembers() != null) {
            // 检查当前成员数量是否超过新的最大成员数
            List<TeamMember> members = teamMemberMapper.listByRegistrationId(teamId);
            if (members.size() > dto.getMaxMembers() - 1) {
                throw new BusinessException("当前成员数量超过新的最大成员数限制");
            }
            updateReg.setMaxMembers(dto.getMaxMembers());
        }
        
        // 更新队长
        if (dto.getLeaderId() != null) {
            // 检查新队长是否存在
            User newLeader = userMapper.getById(dto.getLeaderId());
            if (newLeader == null) {
                throw new BusinessException("新队长不存在");
            }
            
            // 检查新队长是否已经报名该竞赛
            List<Registration> leaderRegistrations = registrationMapper.getByContestIdAndUserId(registration.getContestId(), dto.getLeaderId());
            if (leaderRegistrations != null && !leaderRegistrations.isEmpty() && !leaderRegistrations.get(0).getId().equals(teamId)) {
                throw new BusinessException("新队长已经报名该竞赛");
            }
            
            updateReg.setUserId(dto.getLeaderId());
        }

        if (dto.getStatus()!=null){
            updateReg.setStatus(dto.getStatus());
        }
        
        registrationMapper.update(updateReg);
    }
    
    /**
     * 获取团队详情
     * @param teamId 团队ID
     * @return 团队详情
     */
    @Override
    public TeamDetailVO getTeamDetail(Long teamId) {
        // 获取团队基本信息
        Registration registration = registrationMapper.getById(teamId);
        if (registration == null) {
            throw new BusinessException("团队不存在");
        }
        
        // 检查是否为团队
        if (registration.getIsTeam() == null || !registration.getIsTeam()) {
            throw new BusinessException("该报名记录不是团队");
        }
        
        // 获取竞赛信息
        Contest contest = contestMapper.getById(registration.getContestId());
        
        // 获取队长信息
        User leader = userMapper.getById(registration.getUserId());
        
        // 获取团队成员
        List<TeamMember> teamMembers = teamMemberMapper.listByRegistrationId(teamId);
        List<User> members = new ArrayList<>();
        if (!teamMembers.isEmpty()) {
            for (TeamMember member : teamMembers) {
                User user = userMapper.getById(member.getMemberUserId());
                if (user != null) {
                    members.add(user);
                }
            }
        }
        
        // 构建返回对象
        TeamDetailVO vo = new TeamDetailVO();
        vo.setTeamId(registration.getId());
        vo.setTeamName(registration.getTeamName());
        vo.setTeamDescription(registration.getTeamDescription());
        vo.setMaxMembers(registration.getMaxMembers());
        vo.setContestId(registration.getContestId());
        vo.setContestName(contest != null ? contest.getTitle() : null);
        vo.setStatus(registration.getStatus());
        vo.setCreatedTime(registration.getCreatedTime());
        
        // 设置队长信息
        if (leader != null) {
            UserVO leaderVO = new UserVO();
            leaderVO.setId(leader.getId());
            leaderVO.setUsername(leader.getUsername());
            leaderVO.setEmail(leader.getEmail());
            leaderVO.setPhone(leader.getPhone());
            leaderVO.setRole(leader.getRole());
            vo.setLeader(leaderVO);
        }
        
        // 设置成员信息
        List<UserVO> memberVOs = members.stream().map(member -> {
            UserVO memberVO = new UserVO();
            memberVO.setId(member.getId());
            memberVO.setUsername(member.getUsername());
            memberVO.setEmail(member.getEmail());
            memberVO.setPhone(member.getPhone());
            memberVO.setRole(member.getRole());
            return memberVO;
        }).collect(Collectors.toList());
        vo.setMembers(memberVOs);
        
        return vo;
    }
    
    /**
     * 获取团队列表
     * @param dto 查询参数
     * @return 团队分页列表
     */
    @Override
    public TeamPageVO listTeams(TeamQueryDTO dto) {
        List<Registration> registrations;
        
        // TODO: 此处需要实现基于当前用户角色的判断
        // 由于SecurityUtils存在编译问题，暂时返回所有团队
        if (dto.getContestId() != null) {
            registrations = registrationMapper.listByContestId(dto.getContestId());
            // 过滤出团队类型的报名记录
            registrations = registrations.stream()
                    .filter(r -> r.getIsTeam() != null && r.getIsTeam())
                    .collect(java.util.stream.Collectors.toList());
        } else {
            // 使用新添加的方法查询所有团队
            registrations = registrationMapper.listAllTeams();
        }
        
        // 过滤状态
        if (org.springframework.util.StringUtils.hasText(dto.getStatus())) {
            registrations = registrations.stream()
                    .filter(r -> dto.getStatus().equals(r.getStatus()))
                    .collect(java.util.stream.Collectors.toList());
        }
        
        // 过滤关键词
        if (org.springframework.util.StringUtils.hasText(dto.getKeyword())) {
            registrations = registrations.stream()
                    .filter(r -> r.getTeamName() != null && r.getTeamName().contains(dto.getKeyword()))
                    .collect(java.util.stream.Collectors.toList());
        }
        
        // 构建返回对象
        List<TeamVO> teamVOs = new ArrayList<>();
        for (Registration registration : registrations) {
            TeamVO teamVO = new TeamVO();
            teamVO.setTeamId(registration.getId());
            teamVO.setTeamName(registration.getTeamName());
            teamVO.setLeaderId(registration.getUserId());
            
            // 获取队长名称
            User leader = userMapper.getById(registration.getUserId());
            teamVO.setLeaderName(leader != null ? leader.getUsername() : null);
            
            teamVO.setContestId(registration.getContestId());
            
            // 获取竞赛名称
            Contest contest = contestMapper.getById(registration.getContestId());
            teamVO.setContestName(contest != null ? contest.getTitle() : null);
            
            // 获取成员数量
            List<TeamMember> members = teamMemberMapper.listByRegistrationId(registration.getId());
            teamVO.setMemberCount(members.size() + 1); // 加上队长
            
            teamVO.setStatus(registration.getStatus());
            teamVO.setCreatedTime(registration.getCreatedTime());
            
            teamVOs.add(teamVO);
        }
        
        // 简单分页处理
        int start = (dto.getPage() - 1) * dto.getPageSize();
        int end = Math.min(start + dto.getPageSize(), teamVOs.size());
        
        List<TeamVO> pagedTeamVOs = start < teamVOs.size() ? teamVOs.subList(start, end) : new ArrayList<>();
        
        TeamPageVO pageVO = new TeamPageVO();
        pageVO.setTotal((long) teamVOs.size());
        pageVO.setList(pagedTeamVOs);
        
        return pageVO;
    }
    
    /**
     * 删除团队
     * @param teamId 团队ID
     */
    @Override
    @Transactional
    public void deleteTeam(Long teamId) {
        // 检查团队是否存在
        Registration registration = registrationMapper.getById(teamId);
        if (registration == null) {
            throw new BusinessException("团队不存在");
        }
        
        // 检查是否为团队
        if (registration.getIsTeam() == null || !registration.getIsTeam()) {
            throw new BusinessException("该报名记录不是团队");
        }
        
        // 删除团队成员
        teamMemberMapper.deleteByRegistrationId(teamId);
        
        // 删除团队
        registrationMapper.deleteById(teamId);
    }
    
    /**
     * 添加团队成员
     * @param teamId 团队ID
     * @param dto 添加参数
     */
    @Override
    @Transactional
    public void addTeamMembers(Long teamId, TeamMemberAddDTO dto) {
        // 检查团队是否存在
        Registration registration = registrationMapper.getById(teamId);
        if (registration == null) {
            throw new BusinessException("团队不存在");
        }
        
        // 检查是否为团队
        if (registration.getIsTeam() == null || !registration.getIsTeam()) {
            throw new BusinessException("该报名记录不是团队");
        }
        
        // 只有待审核状态的团队才能添加成员
        if (!"pending".equals(registration.getStatus())) {
            throw new BusinessException("只有待审核状态的团队才能添加成员");
        }
        
        // 获取当前成员列表
        List<TeamMember> currentMembers = teamMemberMapper.listByRegistrationId(teamId);
        
        // 检查是否超过最大成员数
        if (currentMembers.size() + dto.getMemberIds().size() > registration.getMaxMembers() - 1) {
            throw new BusinessException("添加成员后将超过最大成员数限制");
        }
        
        // 添加新成员
        List<TeamMember> newMembers = new ArrayList<>();
        for (Long memberId : dto.getMemberIds()) {
            // 检查成员是否存在
            User member = userMapper.getById(memberId);
            if (member == null) {
                throw new BusinessException("成员不存在: " + memberId);
            }
            
            // 检查是否已经是团队成员
            boolean isExistingMember = currentMembers.stream()
                    .anyMatch(m -> m.getMemberUserId().equals(memberId));
            if (isExistingMember) {
                continue;
            }
            
            // 检查成员是否已经报名该竞赛
            List<Registration> memberRegistrations = registrationMapper.getByContestIdAndUserId(registration.getContestId(), memberId);
            if (memberRegistrations != null && !memberRegistrations.isEmpty()) {
                throw new BusinessException("成员已经报名该竞赛: " + member.getUsername());
            }
            
            TeamMember teamMember = new TeamMember();
            teamMember.setRegistrationId(teamId);
            teamMember.setMemberUserId(memberId);
            newMembers.add(teamMember);
        }
        
        if (!newMembers.isEmpty()) {
            teamMemberMapper.batchInsert(newMembers);
        }
    }
    
    /**
     * 移除团队成员
     * @param teamId 团队ID
     * @param memberId 成员ID
     */
    @Override
    @Transactional
    public void removeTeamMember(Long teamId, Long memberId) {
        // 检查团队是否存在
        Registration registration = registrationMapper.getById(teamId);
        if (registration == null) {
            throw new BusinessException("团队不存在");
        }
        
        // 检查是否为团队
        if (registration.getIsTeam() == null || !registration.getIsTeam()) {
            throw new BusinessException("该报名记录不是团队");
        }
        
        // 只有待审核状态的团队才能移除成员
        if (!"pending".equals(registration.getStatus())) {
            throw new BusinessException("只有待审核状态的团队才能移除成员");
        }
        
        // 检查是否为队长
        if (registration.getUserId().equals(memberId)) {
            throw new BusinessException("不能移除队长");
        }
        
        // 检查成员是否存在于团队中
        TeamMember teamMember = teamMemberMapper.getByRegistrationIdAndMemberUserId(teamId, memberId);
        if (teamMember == null) {
            throw new BusinessException("该成员不在团队中");
        }
        
        // 移除成员
        teamMemberMapper.deleteByRegistrationIdAndMemberUserId(teamId, memberId);
    }
} 