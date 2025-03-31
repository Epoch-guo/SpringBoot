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
 * 题目管理员控制器
 */
@RestController
@RequestMapping("/api/admin/questions")
@Slf4j
@Tag(name = "题目管理员接口", description = "管理员管理题目的相关接口")
public class QuestionAdminController {

    @Autowired
    private QuestionService questionService;

    /**
     * 批量添加题目
     * @param questionDTOList 题目DTO列表
     * @return 添加结果
     */
    @PostMapping("/batch")
    @SaCheckLogin
    @SaCheckRole("admin")
    @Operation(summary = "管理员批量添加题目", description = "管理员批量添加多个题目，需要管理员角色权限")
    public Result<Integer> batchAddQuestions(@RequestBody @Parameter(description = "题目列表") List<QuestionDTO> questionDTOList) {
        log.info("管理员批量添加题目，数量: {}", questionDTOList.size());
        int count = questionService.batchAddQuestions(questionDTOList);
        return Result.success(count, MessageConstant.ADD_SUCCESS);
    }

    /**
     * 获取所有题目列表
     * @return 题目列表
     */
    @GetMapping("/all")
    @SaCheckLogin
    @SaCheckRole("admin")
    @Operation(summary = "管理员获取所有题目列表", description = "管理员获取系统中的所有题目")
    public Result<List<QuestionVO>> getAllQuestions() {
        log.info("管理员获取所有题目列表");
        List<QuestionVO> questions = questionService.getAllQuestions();
        return Result.success(questions);
    }

    /**
     * 管理员更新题目
     * @param questionId 题目ID
     * @param questionDTO 题目DTO
     * @return 更新结果
     */
    @PutMapping("/{questionId}")
    @SaCheckLogin
    @SaCheckRole("admin")
    @Operation(summary = "管理员更新题目", description = "管理员更新题目信息，需要管理员角色权限")
    public Result<String> updateQuestion(
            @PathVariable @Parameter(description = "题目ID") Long questionId,
            @RequestBody @Valid @Parameter(description = "题目信息") QuestionDTO questionDTO) {
        log.info("管理员更新题目，id: {}, 信息: {}", questionId, questionDTO);
        questionDTO.setQuestionId(questionId);
        boolean result = questionService.updateQuestion(questionDTO);
        return result ? Result.success(MessageConstant.UPDATE_SUCCESS) : Result.error(MessageConstant.UPDATE_FAILED);
    }

    /**
     * 管理员删除题目
     * @param questionId 题目ID
     * @return 删除结果
     */
    @DeleteMapping("/{questionId}")
    @SaCheckLogin
    @SaCheckRole("admin")
    @Operation(summary = "管理员删除题目", description = "管理员删除指定题目，需要管理员角色权限")
    public Result<String> deleteQuestion(@PathVariable @Parameter(description = "题目ID") Long questionId) {
        log.info("管理员删除题目，id: {}", questionId);
        boolean result = questionService.deleteQuestion(questionId);
        return result ? Result.success(MessageConstant.DELETE_SUCCESS) : Result.error(MessageConstant.DELETE_FAILED);
    }

    /**
     * 管理员根据竞赛删除题目
     * @param contestId 竞赛ID
     * @return 删除结果
     */
    @DeleteMapping("/contest/{contestId}")
    @SaCheckLogin
    @SaCheckRole("admin")
    @Operation(summary = "管理员删除竞赛题目", description = "管理员删除指定竞赛的所有题目，需要管理员角色权限")
    public Result<Integer> deleteQuestionsByContest(@PathVariable @Parameter(description = "竞赛ID") Long contestId) {
        log.info("管理员删除竞赛题目，竞赛ID: {}", contestId);
        int count = questionService.deleteQuestionsByContestId(contestId);
        return Result.success(count, MessageConstant.DELETE_SUCCESS);
    }
} 