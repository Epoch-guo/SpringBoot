package com.epoch.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 题目实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "题目实体")
public class Question {
    
    @Schema(description = "题目ID")
    private Long id;
    
    @Schema(description = "竞赛ID")
    private Long contestId;
    
    @Schema(description = "题型(choice/coding/design)")
    private String type;
    
    @Schema(description = "题目内容")
    private String content;
    
    @Schema(description = "选择题标准答案")
    private String standardAnswer;
    
    @Schema(description = "主观题评分维度(JSON格式)")
    private String scoringCriteria;
} 