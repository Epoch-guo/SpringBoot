package com.epoch.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 竞赛统计数据展示对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "竞赛统计数据展示对象")
public class ContestStatisticsVO {
    
    @Schema(description = "竞赛ID")
    private Long contestId;
    
    @Schema(description = "竞赛标题")
    private String title;
    
    @Schema(description = "竞赛状态")
    private String status;
    
    @Schema(description = "开始时间")
    private LocalDateTime startTime;
    
    @Schema(description = "结束时间")
    private LocalDateTime endTime;
    
    @Schema(description = "创建者ID")
    private Long creatorId;
    
    @Schema(description = "创建者姓名")
    private String creatorName;
    
    @Schema(description = "报名人数")
    private Integer registrationCount;
    
    @Schema(description = "提交数量")
    private Integer submissionCount;
    
    @Schema(description = "已评分提交数量")
    private Integer scoredSubmissionCount;
    
    @Schema(description = "待评分提交数量")
    private Integer pendingSubmissionCount;
    
    @Schema(description = "平均分数")
    private Double averageScore;
    
    @Schema(description = "最高分数")
    private Integer highestScore;
    
    @Schema(description = "最低分数")
    private Integer lowestScore;
    
    @Schema(description = "评分分布")
    private Map<String, Integer> scoreDistribution;
} 