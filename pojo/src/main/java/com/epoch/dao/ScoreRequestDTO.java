package com.epoch.dao;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 评分请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "评分请求参数")
public class ScoreRequestDTO {
    
    @Schema(description = "提交ID")
    private Long submissionId;
    
    @Schema(description = "分数")
    private Integer score;
    
    @Schema(description = "评语")
    private String comment;
} 