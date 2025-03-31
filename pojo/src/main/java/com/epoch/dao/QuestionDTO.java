package com.epoch.dao;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 题目DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "题目创建请求参数")
public class QuestionDTO {
    
    @Schema(description = "题型(choice/coding/design)", required = true)
    private String type;
    
    @Schema(description = "题目内容", required = true)
    private String content;
    
    @Schema(description = "选择题标准答案")
    private String standardAnswer;
    
    @Schema(description = "选择题选项")
    private List<String> options;
    
    @Schema(description = "主观题评分维度")
    private Map<String, Integer> scoringCriteria;
} 