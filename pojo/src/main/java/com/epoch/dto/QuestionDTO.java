package com.epoch.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



/**
 * 题目数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "题目数据传输对象")
public class QuestionDTO {
    
    @Schema(description = "题目ID")
    private Long questionId;
    
    @NotNull(message = "竞赛ID不能为空")
    @Schema(description = "竞赛ID", required = true)
    private Long contestId;
    
    @NotBlank(message = "题型不能为空")
    @Schema(description = "题型(choice/coding/design)", required = true)
    private String type;
    
    @NotBlank(message = "题目内容不能为空")
    @Schema(description = "题目内容", required = true)
    private String content;
    
    @Schema(description = "选择题标准答案")
    private String standardAnswer;
    
    @Schema(description = "主观题评分维度(JSON格式)")
    private String scoringCriteria;
} 