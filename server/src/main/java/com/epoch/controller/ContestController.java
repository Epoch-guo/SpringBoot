package com.epoch.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.epoch.constant.MessageConstant;
import com.epoch.dao.ContestDTO;
import com.epoch.dao.RegistrationDTO;
import com.epoch.result.PageResult;
import com.epoch.result.Result;
import com.epoch.service.ContestService;
import com.epoch.vo.ContestListVO;
import com.epoch.vo.ContestVO;
import com.epoch.vo.RegistrationVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 竞赛控制器
 */
@RestController
@RequestMapping("/api")
@Slf4j
@Tag(name = "竞赛相关接口", description = "包含竞赛管理、报名等接口")
public class ContestController {

    @Autowired
    private ContestService contestService;

    /**
     * 获取首页竞赛信息（公开接口，无需权限）
     * @param limit 获取条数
     * @return 竞赛列表和统计信息
     */
    @GetMapping("/public/home/contests")
    @Operation(summary = "获取首页竞赛信息", description = "官网首页展示的竞赛数据，包含进行中竞赛和竞赛统计")
    public Result<Map<String, Object>> getHomeContests(@RequestParam(defaultValue = "5") @Parameter(description = "获取条数") Integer limit) {
        log.info("获取首页竞赛信息，limit = {}", limit);
        
        // 获取进行中的竞赛
        List<ContestListVO> ongoingContests = contestService.getOngoingContests(limit);
        
        // 获取竞赛统计信息
        int totalContests = contestService.countAllContests();
        int ongoingCount = contestService.countContestsByStatus("ongoing");
        int endedCount = contestService.countContestsByStatus("ended");
        int pendingCount = contestService.countContestsByStatus("pending");
        
        Map<String, Object> result = new HashMap<>();
        result.put("ongoingContests", ongoingContests);
        result.put("totalContests", totalContests);
        result.put("ongoingCount", ongoingCount);
        result.put("endedCount", endedCount);
        result.put("pendingCount", pendingCount);
        
        return Result.success(result);
    }

    /**
     * 获取竞赛详情（公开接口，无需权限）
     * @param id 竞赛ID
     * @return 竞赛详情
     */
    @GetMapping("/public/contests/{id}")
    @Operation(summary = "获取竞赛公开详情", description = "获取竞赛的公开详细信息，无需登录")
    public Result<ContestVO> getPublicContestDetail(@PathVariable @Parameter(description = "竞赛ID") Long id) {
        log.info("获取公开竞赛详情：{}", id);
        ContestVO contestVO = contestService.getContestDetail(id);
        return Result.success(contestVO);
    }

    /**
     * 创建竞赛
     * @param contestDTO 竞赛信息
     * @return 创建结果
     */
    @PostMapping("/teacher/contests")
    @Operation(summary = "创建竞赛", description = "教师创建新竞赛")

    @SaCheckPermission("contest:create")
    public Result<Map<String, Object>> createContest(@RequestBody @Parameter(description = "竞赛信息") ContestDTO contestDTO) {
        log.info("创建竞赛：{}", contestDTO.getTitle());
        Long creatorId = StpUtil.getLoginIdAsLong();
        Long contestId = contestService.createContest(contestDTO, creatorId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("contestId", contestId);
        
        return Result.success(data, MessageConstant.CONTEST_CREATE_SUCCESS);
    }

    /**
     * 更新竞赛
     * @param id 竞赛ID
     * @param contestDTO 竞赛信息
     * @return 更新结果
     */
    @PutMapping("/teacher/contests/{id}")
    @Operation(summary = "更新竞赛", description = "教师更新竞赛信息")
    @SaCheckPermission("contest:edit")
    public Result<String> updateContest(@PathVariable @Parameter(description = "竞赛ID") Long id, 
                                      @RequestBody @Parameter(description = "竞赛信息") ContestDTO contestDTO) {
        log.info("更新竞赛：{}", id);
        boolean success = contestService.updateContest(id, contestDTO);
        
        return Result.success(null, MessageConstant.CONTEST_UPDATE_SUCCESS);
    }

    /**
     * 获取竞赛详情
     * @param id 竞赛ID
     * @return 竞赛详情
     */
    @GetMapping("/contests/{id}")
    @Operation(summary = "获取竞赛详情", description = "获取竞赛详细信息")
    @SaCheckPermission("contest:view")
    public Result<ContestVO> getContestDetail(@PathVariable @Parameter(description = "竞赛ID") Long id) {
        ContestVO contestVO = contestService.getContestDetail(id);
        
        return Result.success(contestVO);
    }

    /**
     * 分页查询竞赛列表
     * @param page 页码
     * @param pageSize 每页数量
     * @param status 状态
     * @return 分页结果
     */
    @GetMapping("/contests")
    @Operation(summary = "获取竞赛列表", description = "分页查询可报名的竞赛列表")
    @SaCheckPermission("contest:view")
    public Result<PageResult> pageQuery(@RequestParam(defaultValue = "1") @Parameter(description = "页码") Integer page,
                                      @RequestParam(defaultValue = "10") @Parameter(description = "每页数量") Integer pageSize,
                                      @RequestParam(required = false) @Parameter(description = "竞赛状态") String status) {
        PageResult pageResult = contestService.pageQuery(page, pageSize, status);
        
        return Result.success(pageResult);
    }

    /**
     * 查询教师创建的竞赛列表
     * @return 竞赛列表
     */
    @GetMapping("/teacher/contests")
    @Operation(summary = "获取教师创建的竞赛列表", description = "查询当前教师创建的所有竞赛")
    @SaCheckPermission("contest:create")
    public Result<List<ContestListVO>> listByCreator() {
        Long creatorId = StpUtil.getLoginIdAsLong();
        log.info("查询教师创建的竞赛列表：creatorId = {}", creatorId);
        List<ContestListVO> contestListVOS = contestService.listByCreator(creatorId);
        
        return Result.success(contestListVOS);
    }

    /**
     * 更新竞赛状态
     * @param id 竞赛ID
     * @param status 状态
     * @return 更新结果
     */
    @PutMapping("/teacher/contests/{id}/status")
    @Operation(summary = "更新竞赛状态", description = "教师更新竞赛状态")
    @SaCheckPermission("contest:edit")
    public Result<String> updateStatus(@PathVariable @Parameter(description = "竞赛ID") Long id,
                                     @RequestParam @Parameter(description = "状态") String status) {
        log.info("更新竞赛状态：id = {}, status = {}", id, status);
        boolean success = contestService.updateStatus(id, status);
        
        return Result.success(null, MessageConstant.CONTEST_STATUS_UPDATE_SUCCESS);
    }

    /**
     * 删除竞赛
     * @param id 竞赛ID
     * @return 删除结果
     */
    @DeleteMapping("/teacher/contests/{id}")
    @Operation(summary = "删除竞赛", description = "教师删除竞赛")
    @SaCheckPermission("contest:edit")
    public Result<String> deleteContest(@PathVariable @Parameter(description = "竞赛ID") Long id) {
        log.info("删除竞赛：{}", id);
        boolean success = contestService.deleteContest(id);
        
        return Result.success(null, MessageConstant.CONTEST_DELETE_SUCCESS);
    }

    /**
     * 竞赛报名
     * @param id 竞赛ID
     * @param registrationDTO 报名信息
     * @param attachment 附件
     * @return 报名结果
     */
    @PostMapping("/contests/{id}/register")
    @Operation(summary = "竞赛报名", description = "学生个人或团队报名竞赛")
    @SaCheckPermission("contest:view")
    public Result<Map<String, Object>> register(@PathVariable @Parameter(description = "竞赛ID") Long id,
                                              @RequestBody @Parameter(description = "报名信息") RegistrationDTO registrationDTO,
                                              @RequestParam(required = false) @Parameter(description = "报名附件") MultipartFile attachment) {
        log.info("竞赛报名：contestId = {}, teamName = {}", id, registrationDTO.getTeamName());
        Long userId = StpUtil.getLoginIdAsLong();
        Long registrationId = contestService.register(id, registrationDTO, userId, attachment);
        
        Map<String, Object> data = new HashMap<>();
        data.put("registrationId", registrationId);
        data.put("status", "pending");
        
        return Result.success(data, MessageConstant.REGISTRATION_SUCCESS);
    }

    /**
     * 获取报名详情
     * @param id 报名ID
     * @return 报名详情
     */
    @GetMapping("/registrations/{id}")
    @Operation(summary = "获取报名详情", description = "获取报名详细信息")
    @SaCheckPermission("contest:view")
    public Result<RegistrationVO> getRegistrationDetail(@PathVariable @Parameter(description = "报名ID") Long id) {
        log.info("获取报名详情：{}", id);
        RegistrationVO registrationVO = contestService.getRegistrationDetail(id);
        
        return Result.success(registrationVO);
    }

    /**
     * 查询竞赛报名列表
     * @param id 竞赛ID
     * @return 报名列表
     */
    @GetMapping("/teacher/contests/{id}/registrations")
    @Operation(summary = "获取竞赛报名列表", description = "查询竞赛的所有报名记录")
    @SaCheckPermission("contest:edit")
    public Result<List<RegistrationVO>> listRegistrations(@PathVariable @Parameter(description = "竞赛ID") Long id) {
        log.info("查询竞赛报名列表：contestId = {}", id);
        List<RegistrationVO> registrationVOS = contestService.listRegistrations(id);
        
        return Result.success(registrationVOS);
    }

    /**
     * 审核报名
     * @param id 报名ID
     * @param status 状态
     * @return 审核结果
     */
    @PutMapping("/teacher/registrations/{id}/status")
    @Operation(summary = "审核报名", description = "教师审核学生报名")
    @SaCheckPermission("contest:edit")
    public Result<String> auditRegistration(@PathVariable @Parameter(description = "报名ID") Long id,
                                          @RequestParam @Parameter(description = "状态") String status) {
        log.info("审核报名：id = {}, status = {}", id, status);
        boolean success = contestService.auditRegistration(id, status);
        
        return Result.success(null, MessageConstant.REGISTRATION_AUDIT_SUCCESS);
    }

    /**
     * 分页查询公开竞赛列表（无需权限）
     * @param page 页码
     * @param pageSize 每页数量
     * @param status 状态
     * @return 分页结果
     */
    @GetMapping("/public/contests")
    @Operation(summary = "获取公开竞赛列表", description = "分页查询公开的竞赛列表，无需登录")
    public Result<PageResult> publicPageQuery(
            @RequestParam(defaultValue = "1") @Parameter(description = "页码") Integer page,
            @RequestParam(defaultValue = "10") @Parameter(description = "每页数量") Integer pageSize,
            @RequestParam(required = false) @Parameter(description = "竞赛状态") String status) {
        log.info("查询公开竞赛列表：page = {}, pageSize = {}, status = {}", page, pageSize, status);
        PageResult pageResult = contestService.pageQuery(page, pageSize, status);
        return Result.success(pageResult);
    }
    
    /**
     * 获取当前学生报名的竞赛列表
     * @return 竞赛列表
     */
    @GetMapping("/student/contests")
    @Operation(summary = "获取学生报名的竞赛列表", description = "获取当前登录学生报名的所有竞赛")
    @SaCheckPermission("contest:view")
    public Result<List<ContestVO>> getMyContests() {
        Long studentId = StpUtil.getLoginIdAsLong();
        log.info("查询学生报名的竞赛列表：studentId = {}", studentId);
        List<ContestVO> contestVOList = contestService.getContestsByStudentId(studentId);
        return Result.success(contestVOList);
    }
} 