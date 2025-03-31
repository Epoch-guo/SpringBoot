package com.epoch.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.epoch.config.LoggingConfig;
import com.epoch.constant.MessageConstant;
import com.epoch.dto.ScoreDTO;
import com.epoch.result.Result;
import com.epoch.service.ScoreService;
import com.epoch.service.SubmissionService;
import com.epoch.service.UserService;
import com.epoch.service.ContestService;
import com.epoch.utils.UserUtils;
import com.epoch.vo.ScoreVO;
import com.epoch.vo.SubmissionDetailVO;
import com.epoch.vo.UserVO;
import com.epoch.vo.ContestVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 评分控制器
 */
@RestController
@RequestMapping("/api/scores")
@Tag(name = "评分接口", description = "包含评分的增删改查等操作")
public class ScoreController {

    private static final Logger log = LoggingConfig.getLogger(ScoreController.class);

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private SubmissionService submissionService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ContestService contestService;

    /**
     * 教师评分
     * @param scoreDTO 评分DTO
     * @return 添加结果
     */
    @PostMapping
    @SaCheckLogin
    @SaCheckRole("teacher")
    @Operation(summary = "教师评分", description = "教师对学生提交进行评分，需要教师角色权限")
    public Result<Long> addScore(@RequestBody @Valid @Parameter(description = "评分信息") ScoreDTO scoreDTO) {
        log.info("教师评分: {}", scoreDTO);
        Long teacherId = StpUtil.getLoginIdAsLong();
        Long scoreId = scoreService.addScore(scoreDTO, teacherId);
        return Result.success(scoreId, MessageConstant.SCORE_SUCCESS);
    }

    /**
     * 更新评分
     * @param scoreId 评分ID
     * @param scoreDTO 评分DTO
     * @return 更新结果
     */
    @PutMapping("/{scoreId}")
    @SaCheckLogin
    @SaCheckRole("teacher")
    @Operation(summary = "更新评分", description = "教师更新评分信息，需要教师角色权限")
    public Result<String> updateScore(
            @PathVariable @Parameter(description = "评分ID") Long scoreId,
            @RequestBody @Valid @Parameter(description = "评分信息") ScoreDTO scoreDTO) {
        log.info("更新评分，id: {}, 信息: {}", scoreId, scoreDTO);
        scoreDTO.setScoreId(scoreId);
        boolean result = scoreService.updateScore(scoreDTO);
        return result ? Result.success(MessageConstant.UPDATE_SUCCESS) : Result.error(MessageConstant.UPDATE_FAILED);
    }

    /**
     * 获取评分详情
     * @param scoreId 评分ID
     * @return 评分信息
     */
    @GetMapping("/{scoreId}")
    @SaCheckLogin
    @Operation(summary = "获取评分详情", description = "根据ID获取评分详细信息")
    public Result<ScoreVO> getScore(@PathVariable @Parameter(description = "评分ID") Long scoreId) {
        log.info("获取评分信息，id: {}", scoreId);
        ScoreVO score = scoreService.getScoreById(scoreId);
        return score != null ? Result.success(score) : Result.error(MessageConstant.DATA_NOT_FOUND);
    }

    /**
     * 获取提交的评分
     * @param submissionId 提交ID
     * @return 评分信息
     */
    @GetMapping("/submission/{submissionId}")
    @SaCheckLogin
    @Operation(summary = "获取提交的评分", description = "根据提交ID获取评分信息")
    public Result<ScoreVO> getScoreBySubmission(@PathVariable @Parameter(description = "提交ID") Long submissionId) {
        log.info("获取提交评分，提交ID: {}", submissionId);
        ScoreVO score = scoreService.getScoreBySubmissionId(submissionId);
        return score != null ? Result.success(score) : Result.success(null);
    }

    /**
     * 获取竞赛评分列表
     * @param contestId 竞赛ID
     * @return 评分列表
     */
    @SaCheckLogin
    @GetMapping("/contest/{contestId}")
    @Operation(summary = "获取竞赛评分列表", description = "获取指定竞赛的所有评分")
    public Result<List<ScoreVO>> getScoresByContest(@PathVariable @Parameter(description = "竞赛ID") Long contestId) {
        log.info("获取竞赛ID为{}的所有评分", contestId);
        // 使用视图查询更高效
        List<ScoreVO> scoreVOS = scoreService.getScoresByContestIdFromView(contestId);
        return Result.success(scoreVOS);
    }

    /**
     * 获取当前教师在特定竞赛中的所有评分
     * @param contestId 竞赛ID
     * @return 评分列表
     */
    @SaCheckLogin
    @SaCheckRole("teacher")
    @GetMapping("/teacher/contest/{contestId}")
    public Result<List<ScoreVO>> getTeacherScoresByContest(@PathVariable Long contestId) {
        log.info("获取当前教师在竞赛ID为{}的所有评分", contestId);
        Long teacherId = StpUtil.getLoginIdAsLong();
        List<ScoreVO> scoreVOS = scoreService.getScoresByContestIdAndTeacherId(contestId, teacherId);
        return Result.success(scoreVOS);
    }

    /**
     * 获取教师评分列表
     * @return 评分列表
     */
    @GetMapping("/teacher")
    @SaCheckLogin
    @SaCheckRole("teacher")
    @Operation(summary = "获取教师评分列表", description = "获取当前登录教师的所有评分")
    public Result<List<ScoreVO>> getTeacherScores() {
        Long teacherId = StpUtil.getLoginIdAsLong();
        log.info("获取教师评分列表，教师ID: {}", teacherId);
        List<ScoreVO> scores = scoreService.getScoresByTeacherId(teacherId);
        return Result.success(scores);
    }

    /**
     * 获取学生被评分列表
     * @return 评分列表
     */
    @GetMapping("/student")
    @SaCheckLogin
    @SaCheckRole("student")
    @Operation(summary = "获取学生被评分列表", description = "获取当前登录学生的所有被评分")
    public Result<List<ScoreVO>> getStudentScores() {
        Long studentId = StpUtil.getLoginIdAsLong();
        log.info("获取学生被评分列表，学生ID: {}", studentId);
        List<ScoreVO> scores = scoreService.getScoresByStudentId(studentId);
        return Result.success(scores);
    }

    /**
     * 删除评分
     * @param scoreId 评分ID
     * @return 删除结果
     */
    @DeleteMapping("/{scoreId}")
    @SaCheckLogin
    @SaCheckRole("teacher")
    @Operation(summary = "删除评分", description = "删除指定评分，需要教师角色权限")
    public Result<String> deleteScore(@PathVariable @Parameter(description = "评分ID") Long scoreId) {
        log.info("删除评分，id: {}", scoreId);
        boolean result = scoreService.deleteScore(scoreId);
        return result ? Result.success(MessageConstant.DELETE_SUCCESS) : Result.error(MessageConstant.DELETE_FAILED);
    }

    /**
     * 获取竞赛待评分提交列表
     * @param contestId 竞赛ID
     * @return 提交列表
     */
    @GetMapping("/pending/{contestId}")
    @SaCheckLogin
    @SaCheckRole("teacher")
    @Operation(summary = "获取待评分提交列表", description = "获取指定竞赛的待评分提交列表")
    public Result<List<SubmissionDetailVO>> getPendingSubmissions(@PathVariable @Parameter(description = "竞赛ID") Long contestId) {
        log.info("获取竞赛{}的待评分提交列表", contestId);
        List<SubmissionDetailVO> submissions = submissionService.getPendingSubmissions(contestId);
        return Result.success(submissions);
    }

    /**
     * 获取竞赛已评分提交列表
     * @param contestId 竞赛ID
     * @return 提交列表
     */
    @GetMapping("/scored/{contestId}")
    @SaCheckLogin
    @SaCheckRole("teacher")
    @Operation(summary = "获取已评分提交列表", description = "获取指定竞赛的已评分提交列表")
    public Result<List<SubmissionDetailVO>> getScoredSubmissions(@PathVariable @Parameter(description = "竞赛ID") Long contestId) {
        log.info("获取竞赛{}的已评分提交列表", contestId);
        List<SubmissionDetailVO> submissions = submissionService.getScoredSubmissions(contestId);
        return Result.success(submissions);
    }

    /**
     * 获取评分流程数据
     * 按照学生-比赛-提交的层次结构返回数据
     * @return 评分流程数据
     */
    @GetMapping("/scoring-process")
    @SaCheckLogin
    @SaCheckRole("teacher")
    @Operation(summary = "获取评分流程数据", description = "按照学生-比赛-提交的层次结构获取待评分的内容")
    public Result<Map<String, Object>> getScoringProcessData() {
        log.info("获取评分流程数据");
        Map<String, Object> result = new HashMap<>();
        
        // 1. 获取所有学生
        List<UserVO> students = userService.getAllStudents();
        result.put("students", students);
        
        // 2. 获取所有活跃的竞赛
        List<ContestVO> activeContests = contestService.getActiveContests();
        result.put("contests", activeContests);
        
        // 3. 教师只能看到自己创建的竞赛的提交
        Long teacherId = UserUtils.getCurrentUserId();
        List<ContestVO> teacherContests = activeContests.stream()
                .filter(contest -> contest.getCreatorId().equals(teacherId))
                .collect(Collectors.toList());
        
        // 4. 获取每个竞赛的待评分提交
        Map<Long, List<SubmissionDetailVO>> contestSubmissions = new HashMap<>();
        for (ContestVO contest : teacherContests) {
            List<SubmissionDetailVO> pendingSubmissions = submissionService.getPendingSubmissions(contest.getContestId());
            if (!pendingSubmissions.isEmpty()) {
                contestSubmissions.put(contest.getContestId(), pendingSubmissions);
            }
        }
        result.put("contestSubmissions", contestSubmissions);
        
        return Result.success(result);
    }
    
    /**
     * 根据学生ID获取该学生参加的所有竞赛
     * @param studentId 学生ID
     * @return 竞赛列表
     */
    @GetMapping("/student/{studentId}/contests")
    @SaCheckLogin
    @SaCheckRole("teacher")
    @Operation(summary = "获取学生参加的竞赛", description = "根据学生ID获取该学生参加的所有竞赛")
    public Result<List<ContestVO>> getStudentContests(@PathVariable @Parameter(description = "学生ID") Long studentId) {
        log.info("获取学生ID为{}参加的所有竞赛", studentId);
        List<ContestVO> contests = contestService.getContestsByStudentId(studentId);
        return Result.success(contests);
    }
    
    /**
     * 根据学生ID和竞赛ID获取该学生在该竞赛的所有提交
     * @param studentId 学生ID
     * @param contestId 竞赛ID
     * @return 提交列表
     */
    @GetMapping("/student/{studentId}/contest/{contestId}/submissions")
    @SaCheckLogin
    @SaCheckRole("teacher")
    @Operation(summary = "获取学生在特定竞赛的提交", description = "根据学生ID和竞赛ID获取该学生在该竞赛的所有提交")
    public Result<List<SubmissionDetailVO>> getStudentContestSubmissions(
            @PathVariable @Parameter(description = "学生ID") Long studentId,
            @PathVariable @Parameter(description = "竞赛ID") Long contestId) {
        log.info("获取学生ID为{}在竞赛ID为{}的所有提交", studentId, contestId);
        List<SubmissionDetailVO> submissions = submissionService.getSubmissionsByStudentAndContest(studentId, contestId);
        return Result.success(submissions);
    }
} 