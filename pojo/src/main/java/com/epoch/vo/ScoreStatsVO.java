package com.epoch.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 成绩统计VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "成绩统计信息")
public class ScoreStatsVO {
    
    @Schema(description = "平均分")
    private Double averageScore;
    
    @Schema(description = "最高分")
    private Integer maxScore;
    
    @Schema(description = "最低分")
    private Integer minScore;
    
    @Schema(description = "分数分布")
    private Map<String, Integer> distribution;
} 