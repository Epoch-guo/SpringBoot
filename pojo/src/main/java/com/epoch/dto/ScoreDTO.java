package com.epoch.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * 评分数据传输对象
 */
@Data
@Schema(description = "评分数据传输对象")
public class ScoreDTO {
    
    @Schema(description = "评分ID，更新时必填")
    private Long scoreId;
    
    @Schema(description = "提交ID，新增时必填")
    @NotNull(message = "提交ID不能为空")
    private Long submissionId;
    
    @Schema(description = "分数")
    @NotNull(message = "分数不能为空")
    @Min(value = 0, message = "分数不能小于0")
    @Max(value = 100, message = "分数不能大于100")
    private Integer manualScore;
    
    @Schema(description = "评语")
    private String comments;
} 