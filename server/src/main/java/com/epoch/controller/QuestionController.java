package com.epoch.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.epoch.constant.MessageConstant;
import com.epoch.dto.QuestionDTO;
import com.epoch.result.Result;
import com.epoch.service.QuestionService;
import com.epoch.vo.QuestionVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 题目管理控制器
 */
@RestController
@RequestMapping("/api/questions")
@Slf4j
@Tag(name = "题目管理接口", description = "包含题目的增删改查等操作")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    /**
     * 添加题目
     * @param questionDTO 题目DTO
     * @return 添加结果
     */
    @PostMapping
    @SaCheckLogin
    @SaCheckRole("teacher")
    @Operation(summary = "添加题目", description = "添加新题目，需要教师角色权限")
    public Result<Long> addQuestion(@RequestBody @Valid @Parameter(description = "题目信息") QuestionDTO questionDTO) {
        log.info("添加题目: {}", questionDTO);
        Long questionId = questionService.addQuestion(questionDTO);
        return Result.success(questionId, MessageConstant.ADD_SUCCESS);
    }

    /**
     * 批量添加题目
     * @param questionDTOList 题目DTO列表
     * @return 添加结果
     */
    @PostMapping("/batch")
    @SaCheckLogin
    @SaCheckRole("teacher")
    @Operation(summary = "批量添加题目", description = "批量添加多个题目，需要教师角色权限")
    public Result<Integer> batchAddQuestions(@RequestBody @Parameter(description = "题目列表") List<QuestionDTO> questionDTOList) {
        log.info("批量添加题目，数量: {}", questionDTOList.size());
        int count = questionService.batchAddQuestions(questionDTOList);
        return Result.success(count, MessageConstant.ADD_SUCCESS);
    }

    /**
     * 更新题目
     * @param questionId 题目ID
     * @param questionDTO 题目DTO
     * @return 更新结果
     */
    @PutMapping("/{questionId}")
    @SaCheckLogin
    @SaCheckRole("teacher")
    @Operation(summary = "更新题目", description = "更新题目信息，需要教师角色权限")
    public Result<String> updateQuestion(
            @PathVariable @Parameter(description = "题目ID") Long questionId,
            @RequestBody @Valid @Parameter(description = "题目信息") QuestionDTO questionDTO) {
        log.info("更新题目，id: {}, 信息: {}", questionId, questionDTO);
        questionDTO.setQuestionId(questionId);
        boolean result = questionService.updateQuestion(questionDTO);
        return result ? Result.success(MessageConstant.UPDATE_SUCCESS) : Result.error(MessageConstant.UPDATE_FAILED);
    }

    /**
     * 获取题目
     * @param questionId 题目ID
     * @return 题目信息
     */
    @GetMapping("/{questionId}")
    @SaCheckLogin
    @Operation(summary = "获取题目详情", description = "根据ID获取题目详细信息")
    public Result<QuestionVO> getQuestion(@PathVariable @Parameter(description = "题目ID") Long questionId) {
        log.info("获取题目信息，id: {}", questionId);
        QuestionVO question = questionService.getQuestionById(questionId);
        return question != null ? Result.success(question) : Result.error(MessageConstant.DATA_NOT_FOUND);
    }
    
    /**
     * 获取所有题目列表
     * @return 题目列表
     */
    @GetMapping("/all")
    @SaCheckLogin
    @Operation(summary = "获取所有题目列表", description = "获取系统中的所有题目")
    public Result<List<QuestionVO>> getAllQuestions() {
        log.info("获取所有题目列表");
        List<QuestionVO> questions = questionService.getAllQuestions();
        return Result.success(questions);
    }

    /**
     * 获取竞赛题目列表
     * @param contestId 竞赛ID
     * @return 题目列表
     */
    @GetMapping("/contest/{contestId}")
    @SaCheckLogin
    @Operation(summary = "获取竞赛题目列表", description = "获取指定竞赛的所有题目")
    public Result<List<QuestionVO>> getQuestionsByContest(@PathVariable @Parameter(description = "竞赛ID") Long contestId) {
        log.info("获取竞赛题目列表，竞赛ID: {}", contestId);
        List<QuestionVO> questions = questionService.getQuestionsByContestId(contestId);
        return Result.success(questions);
    }

    /**
     * 删除题目
     * @param questionId 题目ID
     * @return 删除结果
     */
    @DeleteMapping("/{questionId}")
    @SaCheckLogin
    @SaCheckRole("teacher")
    @Operation(summary = "删除题目", description = "删除指定题目，需要教师角色权限")
    public Result<String> deleteQuestion(@PathVariable @Parameter(description = "题目ID") Long questionId) {
        log.info("删除题目，id: {}", questionId);
        boolean result = questionService.deleteQuestion(questionId);
        return result ? Result.success(MessageConstant.DELETE_SUCCESS) : Result.error(MessageConstant.DELETE_FAILED);
    }

    /**
     * 根据竞赛删除题目
     * @param contestId 竞赛ID
     * @return 删除结果
     */
    @DeleteMapping("/contest/{contestId}")
    @SaCheckLogin
    @SaCheckRole("teacher")
    @Operation(summary = "删除竞赛题目", description = "删除指定竞赛的所有题目，需要教师角色权限")
    public Result<Integer> deleteQuestionsByContest(@PathVariable @Parameter(description = "竞赛ID") Long contestId) {
        log.info("删除竞赛题目，竞赛ID: {}", contestId);
        int count = questionService.deleteQuestionsByContestId(contestId);
        return Result.success(count, MessageConstant.DELETE_SUCCESS);
    }
} 