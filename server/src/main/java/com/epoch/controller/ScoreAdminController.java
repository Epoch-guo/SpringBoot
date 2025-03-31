package com.epoch.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.epoch.config.LoggingConfig;
import com.epoch.constant.MessageConstant;
import com.epoch.dto.ScoreDTO;
import com.epoch.result.Result;
import com.epoch.service.ScoreService;
import com.epoch.vo.ScoreVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 评分管理员控制器
 */
@RestController
@RequestMapping("/api/admin/scores")
@Tag(name = "评分管理员接口", description = "管理员管理评分的相关接口")
public class ScoreAdminController {

    private static final Logger log = LoggingConfig.getLogger(ScoreAdminController.class);

    @Autowired
    private ScoreService scoreService;

    /**
     * 获取所有竞赛评分列表
     * @return 评分列表
     */
    @GetMapping("/all")
    @SaCheckLogin
    @SaCheckRole("admin")
    @Operation(summary = "管理员获取所有评分", description = "管理员获取系统中的所有评分")
    public Result<List<ScoreVO>> getAllScores() {
        log.info("管理员获取所有评分列表");
        // 实际场景可能需要分页和筛选，这里简化处理，先获取所有竞赛ID
        List<Long> contestIds = List.of(1L, 2L, 3L); // 这里应该动态获取所有竞赛ID
        
        // 这里简化处理，只获取第一个竞赛的评分
        List<ScoreVO> scores = scoreService.getScoresByContestIdFromView(contestIds.get(0));
        return Result.success(scores);
    }

    /**
     * 获取竞赛评分列表
     * @param contestId 竞赛ID
     * @return 评分列表
     */
    @SaCheckLogin
    @SaCheckRole("admin")
    @GetMapping("/scores/contest/{contestId}")
    @Operation(summary = "获取竞赛评分列表", description = "管理员获取指定竞赛的所有评分")
    public Result<List<ScoreVO>> getScoresByContest(@PathVariable @Parameter(description = "竞赛ID") Long contestId) {
        log.info("管理员获取竞赛ID为{}的所有评分", contestId);
        // 使用视图查询更高效
        List<ScoreVO> scoreVOS = scoreService.getScoresByContestIdFromView(contestId);
        return Result.success(scoreVOS);
    }

    /**
     * 更新评分
     * @param scoreId 评分ID
     * @param scoreDTO 评分DTO
     * @return 更新结果
     */
    @PutMapping("/{scoreId}")
    @SaCheckLogin
    @SaCheckRole("admin")
    @Operation(summary = "管理员更新评分", description = "管理员更新评分信息，需要管理员角色权限")
    public Result<String> updateScore(
            @PathVariable @Parameter(description = "评分ID") Long scoreId,
            @RequestBody @Valid @Parameter(description = "评分信息") ScoreDTO scoreDTO) {
        log.info("管理员更新评分，id: {}, 信息: {}", scoreId, scoreDTO);
        scoreDTO.setScoreId(scoreId);
        boolean result = scoreService.updateScore(scoreDTO);
        return result ? Result.success(MessageConstant.UPDATE_SUCCESS) : Result.error(MessageConstant.UPDATE_FAILED);
    }

    /**
     * 删除评分
     * @param scoreId 评分ID
     * @return 删除结果
     */
    @DeleteMapping("/{scoreId}")
    @SaCheckLogin
    @SaCheckRole("admin")
    @Operation(summary = "管理员删除评分", description = "管理员删除指定评分，需要管理员角色权限")
    public Result<String> deleteScore(@PathVariable @Parameter(description = "评分ID") Long scoreId) {
        log.info("管理员删除评分，id: {}", scoreId);
        boolean result = scoreService.deleteScore(scoreId);
        return result ? Result.success(MessageConstant.DELETE_SUCCESS) : Result.error(MessageConstant.DELETE_FAILED);
    }
} 