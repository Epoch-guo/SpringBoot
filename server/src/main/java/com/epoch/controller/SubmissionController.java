package com.epoch.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.stp.StpUtil;
import com.epoch.config.LoggingConfig;
import com.epoch.constant.MessageConstant;
import com.epoch.dao.SubmissionDTO;
import com.epoch.dto.ScoreDTO;
import com.epoch.result.Result;
import com.epoch.service.SubmissionService;
import com.epoch.vo.SubmissionVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提交记录控制器
 */
@RestController
@RequestMapping("/api")
@Tag(name = "题目提交相关接口", description = "包含题目提交、查询、评分等接口")
public class SubmissionController {

    private static final Logger log = LoggingConfig.getLogger(SubmissionController.class);

    @Autowired
    private SubmissionService submissionService;

    /**
     * 提交题目
     * @param submissionDTO 提交信息
     * @param file 提交文件
     * @return 提交结果
     */
    @PostMapping("/submissions")
    @Operation(summary = "提交题目", description = "学生提交题目答案")
    @SaCheckPermission("submission:create")
    public Result<Map<String, Object>> submitQuestion(@RequestBody @Parameter(description = "提交信息") SubmissionDTO submissionDTO,
                                                    @RequestParam(required = false) @Parameter(description = "提交文件") MultipartFile file) {
        log.info("提交题目：questionId = {}", submissionDTO.getQuestionId());
        Long userId = StpUtil.getLoginIdAsLong();
        Long submissionId = submissionService.submitQuestion(submissionDTO, userId, file);
        
        Map<String, Object> data = new HashMap<>();
        data.put("submissionId", submissionId);
        
        return Result.success(data, MessageConstant.SUBMISSION_SUCCESS);
    }

    /**
     * 获取提交详情
     * @param id 提交记录ID
     * @return 提交详情
     */
    @GetMapping("/submissions/{id}")
    @Operation(summary = "获取提交详情", description = "获取提交记录详细信息")
    @SaCheckPermission(value = {"submission:create", "submission:review"}, mode = SaMode.OR)
    public Result<SubmissionVO> getSubmissionDetail(@PathVariable @Parameter(description = "提交记录ID") Long id) {
        log.info("获取提交详情：{}", id);
        SubmissionVO submissionVO = submissionService.getSubmissionDetail(id);
        
        return Result.success(submissionVO);
    }

    /**
     * 查询当前用户提交记录列表
     * @return 提交记录列表
     */
    @GetMapping("/submissions/my")
    @Operation(summary = "获取我的提交记录", description = "查询当前用户的所有提交记录")
    @SaCheckPermission("submission:create")
    public Result<List<SubmissionVO>> listMySubmissions() {
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("查询用户提交记录列表：userId = {}", userId);
        List<SubmissionVO> submissionVOS = submissionService.listByUserId(userId);
        
        return Result.success(submissionVOS);
    }

    /**
     * 查询题目提交记录列表
     * @param questionId 题目ID
     * @return 提交记录列表
     */
    @GetMapping("/teacher/questions/{questionId}/submissions")
    @Operation(summary = "获取题目提交记录", description = "查询题目的所有提交记录")
    @SaCheckPermission("submission:review")
    public Result<List<SubmissionVO>> listByQuestionId(@PathVariable @Parameter(description = "题目ID") Long questionId) {
        log.info("查询题目提交记录列表：questionId = {}", questionId);
        List<SubmissionVO> submissionVOS = submissionService.listByQuestionId(questionId);
        
        return Result.success(submissionVOS);
    }

    /**
     * 查询竞赛提交记录列表
     * @param contestId 竞赛ID
     * @return 提交记录列表
     */
    @GetMapping("/teacher/contests/{contestId}/submissions")
    @Operation(summary = "获取竞赛提交记录", description = "查询竞赛的所有提交记录")
    @SaCheckPermission("submission:review")
    public Result<List<SubmissionVO>> listByContestId(@PathVariable @Parameter(description = "竞赛ID") Long contestId) {
        log.info("查询竞赛提交记录列表：contestId = {}", contestId);
        List<SubmissionVO> submissionVOS = submissionService.listByContestId(contestId);
        
        return Result.success(submissionVOS);
    }

    /**
     * 评分
     * @param id 提交记录ID
     * @param scoreDTO 评分信息
     * @return 评分结果
     */
    @PutMapping("/teacher/submissions/{id}/score")
    @Operation(summary = "评分", description = "教师对提交记录进行评分")
    @SaCheckPermission("submission:review")
    public Result<String> scoreSubmission(@PathVariable @Parameter(description = "提交记录ID") Long id,
                                        @RequestBody @Parameter(description = "评分信息") ScoreDTO scoreDTO) {
        log.info("评分：submissionId = {}, score = {}", id, scoreDTO.getManualScore());
        Long teacherId = StpUtil.getLoginIdAsLong();
        boolean success = submissionService.scoreSubmission(id, scoreDTO, teacherId);
        
        return Result.success(null, MessageConstant.SCORE_SUCCESS);
    }

    /**
     * 删除提交记录
     * @param id 提交记录ID
     * @return 删除结果
     */
    @DeleteMapping("/teacher/submissions/{id}")
    @Operation(summary = "删除提交记录", description = "教师删除提交记录")
    @SaCheckPermission("submission:review")
    public Result<String> deleteSubmission(@PathVariable @Parameter(description = "提交记录ID") Long id) {
        log.info("删除提交记录：{}", id);
        boolean success = submissionService.deleteSubmission(id);
        
        return Result.success(null, MessageConstant.SUBMISSION_DELETE_SUCCESS);
    }
} 