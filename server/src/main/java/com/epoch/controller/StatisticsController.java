package com.epoch.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.epoch.config.LoggingConfig;
import com.epoch.result.Result;
import com.epoch.service.ContestService;
import com.epoch.service.RegistrationService;
import com.epoch.service.ScoreService;
import com.epoch.service.UserService;
import com.epoch.vo.StatisticsDashboardVO;
import com.epoch.vo.ContestStatisticsVO;
import com.epoch.vo.ScoreRankingVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 数据统计控制器
 */
@RestController
@RequestMapping("/api/statistics")
@Tag(name = "数据统计接口", description = "包含仪表盘统计、竞赛统计、成绩排名等数据统计接口")
public class StatisticsController {

    private static final Logger log = LoggingConfig.getLogger(StatisticsController.class);

    @Autowired
    private ContestService contestService;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private UserService userService;

    /**
     * 获取仪表盘统计数据
     * @return 仪表盘统计数据
     */
    @GetMapping("/dashboard")
    @SaCheckLogin
    @Operation(summary = "获取仪表盘统计数据", description = "获取主页仪表盘展示的统计数据，包括竞赛数量、报名人数、用户数量等")
    public Result<StatisticsDashboardVO> getDashboardStatistics() {
        log.info("获取仪表盘统计数据");
        StatisticsDashboardVO dashboardData = new StatisticsDashboardVO();
        
        // 获取总竞赛数量
        dashboardData.setTotalContests(contestService.countContests());
        
        // 获取总用户数量和各角色数量
        dashboardData.setTotalUsers(userService.countUsers());
        dashboardData.setTotalStudents(userService.countUsersByRole("student"));
        dashboardData.setTotalTeachers(userService.countUsersByRole("teacher"));
        
        // 获取总报名数量
        dashboardData.setTotalRegistrations(registrationService.countRegistrations());
        
        // 获取状态统计
        dashboardData.setOngoingContests(contestService.countContestsByStatus("ongoing"));
        dashboardData.setEndedContests(contestService.countContestsByStatus("ended"));
        dashboardData.setPendingContests(contestService.countContestsByStatus("pending"));
        
        return Result.success(dashboardData);
    }

    /**
     * 获取竞赛统计数据
     * @param contestId 竞赛ID
     * @return 竞赛统计数据
     */
    @GetMapping("/contest/{contestId}")
    @SaCheckLogin
    @Operation(summary = "获取竞赛统计数据", description = "获取特定竞赛的统计数据，包括报名人数、提交数量、评分情况等")
    public Result<ContestStatisticsVO> getContestStatistics(
            @PathVariable @Parameter(description = "竞赛ID") Long contestId) {
        log.info("获取竞赛统计数据: contestId = {}", contestId);
        
        ContestStatisticsVO statistics = contestService.getContestStatistics(contestId);
        return Result.success(statistics);
    }

    /**
     * 获取成绩排名数据
     * @param contestId 竞赛ID
     * @return 成绩排名数据
     */
    @GetMapping("/ranking/{contestId}")
    @SaCheckLogin
    @Operation(summary = "获取成绩排名数据", description = "获取特定竞赛的成绩排名数据，按分数从高到低排序")
    public Result<List<ScoreRankingVO>> getScoreRanking(
            @PathVariable @Parameter(description = "竞赛ID") Long contestId) {
        log.info("获取成绩排名数据: contestId = {}", contestId);
        
        List<ScoreRankingVO> ranking = scoreService.getScoreRanking(contestId);
        return Result.success(ranking);
    }

    /**
     * 获取竞赛参与度统计
     * @return 各竞赛参与度统计数据，包括竞赛名称、报名人数、提交率等
     */
    @GetMapping("/participation")
    @SaCheckRole("admin")
    @Operation(summary = "获取竞赛参与度统计", 
              description = "获取各竞赛的参与度统计数据，包括竞赛名称、报名人数、提交率等")
    @ApiResponse(responseCode = "200", description = "成功", content = {
        @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class),
            examples = @ExampleObject(value = """
                {
                    "code": 200,
                    "msg": "success",
                    "data": [{
                        "contestId": 1,
                        "contestName": "Math Contest",
                        "registrationCount": 10,
                        "submissionRate": 0.8
                    }]
                }
                """))
    })
    public Result<List<Map<String, Object>>> getParticipationStatistics() {
        log.info("获取竞赛参与度统计");
        
        List<Map<String, Object>> statistics = contestService.getParticipationStatistics();
        return Result.success(statistics);
    }

    /**
     * 获取评分分布统计
     * @param contestId 竞赛ID
     * @return 评分分布统计数据
     */
    @GetMapping("/score-distribution/{contestId}")
    @SaCheckLogin
    @Operation(summary = "获取评分分布统计", description = "获取特定竞赛的评分分布统计，按分数段统计人数")
    public Result<Map<String, Integer>> getScoreDistribution(
            @PathVariable @Parameter(description = "竞赛ID") Long contestId) {
        log.info("获取评分分布统计: contestId = {}", contestId);
        
        Map<String, Integer> distribution = scoreService.getScoreDistribution(contestId);
        return Result.success(distribution);
    }
    
    /**
     * 获取教师评分工作量统计
     * @return 教师评分工作量统计数据
     */
    @GetMapping("/teacher-workload")
    @SaCheckRole("admin")
    @Operation(summary = "获取教师评分工作量统计", description = "获取各教师的评分工作量统计数据")
    public Result<List<Map<String, Object>>> getTeacherWorkloadStatistics() {
        log.info("获取教师评分工作量统计");
        
        List<Map<String, Object>> workload = scoreService.getTeacherWorkloadStatistics();
        return Result.success(workload);
    }
} 