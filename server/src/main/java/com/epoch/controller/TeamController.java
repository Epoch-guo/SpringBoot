package com.epoch.controller;

import com.epoch.dto.TeamCreateDTO;
import com.epoch.dto.TeamMemberAddDTO;
import com.epoch.dto.TeamQueryDTO;
import com.epoch.dto.TeamUpdateDTO;
import com.epoch.result.Result;
import com.epoch.service.TeamService;
import com.epoch.service.UserService;
import com.epoch.vo.StudentVO;
import com.epoch.vo.TeamDetailVO;
import com.epoch.vo.TeamPageVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

/**
 * 团队管理Controller
 */
@RestController
@RequestMapping("/api/teams")
@Tag(name = "团队管理接口")
@Slf4j
public class TeamController {
    
    @Autowired
    private TeamService teamService;
    
    @Autowired
    private UserService userService;
    
    /**
     * 创建团队
     * @param dto 创建参数
     * @return 团队ID
     */
    @PostMapping
    @Operation(summary = "创建团队",description = "进行团队的创建")
    public Result<Long> createTeam(@RequestBody @Valid TeamCreateDTO dto) {
        // 使用传入的队长ID，如果没有传入则使用当前登录用户ID
        Long userId = dto.getLeaderId() != null ? dto.getLeaderId() : 1L;
        Long teamId = teamService.createTeam(userId, dto);
        return Result.success(teamId);
    }
    
    /**
     * 更新团队信息
     * @param id 团队ID
     * @param dto 更新参数
     * @return 结果
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新团队信息" ,description = "进行团队信息的编辑")
    public Result<Void> updateTeam(
            @PathVariable("id") @Parameter(description = "团队ID") Long id,
            @RequestBody @Valid TeamUpdateDTO dto) {
        teamService.updateTeam(id, dto);
        return Result.success();
    }
    
    /**
     * 获取团队详情
     * @param id 团队ID
     * @return 团队详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取团队详情",description ="获取团队的详细内容")
    public Result<TeamDetailVO> getTeamDetail(
            @PathVariable("id") @Parameter(description = "团队ID") Long id) {
        TeamDetailVO vo = teamService.getTeamDetail(id);
        return Result.success(vo);
    }
    
    /**
     * 获取团队列表
     * @param dto 查询参数
     * @return 团队分页列表
     */
    @GetMapping
    @Operation(summary = "获取团队列表")
    public Result<TeamPageVO> listTeams(TeamQueryDTO dto) {
        TeamPageVO vo = teamService.listTeams(dto);
        return Result.success(vo);
    }
    
    /**
     * 删除团队
     * @param id 团队ID
     * @return 结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除团队")
    public Result<Void> deleteTeam(
            @PathVariable("id") @Parameter(description = "团队ID") Long id) {
        teamService.deleteTeam(id);
        return Result.success();
    }
    
    /**
     * 添加团队成员
     * @param id 团队ID
     * @param dto 添加参数
     * @return 结果
     */
    @PostMapping("/{id}/members")
    @Operation(summary = "添加团队成员")
    public Result<Void> addTeamMembers(
            @PathVariable("id") @Parameter(description = "团队ID") Long id,
            @RequestBody @Valid TeamMemberAddDTO dto) {
        teamService.addTeamMembers(id, dto);
        return Result.success();
    }
    
    /**
     * 移除团队成员
     * @param id 团队ID
     * @param memberId 成员ID
     * @return 结果
     */
    @DeleteMapping("/{id}/members/{memberId}")
    @Operation(summary = "移除团队成员")
    public Result<Void> removeTeamMember(
            @PathVariable("id") @Parameter(description = "团队ID") Long id,
            @PathVariable("memberId") @Parameter(description = "成员ID") Long memberId) {
        teamService.removeTeamMember(id, memberId);
        return Result.success();
    }
    
    /**
     * 获取学生列表，用于添加团队成员
     * @param contestId 竞赛ID
     * @return 学生列表
     */
    @GetMapping("/students")
    @Operation(summary = "获取学生列表", description = "获取可添加为团队成员的学生列表，标记队长")
    public Result<List<StudentVO>> listStudents(
            @RequestParam(value = "contestId", required = false) @Parameter(description = "竞赛ID") Long contestId) {
        List<StudentVO> students = userService.listStudents(contestId);
        return Result.success(students);
    }
} 