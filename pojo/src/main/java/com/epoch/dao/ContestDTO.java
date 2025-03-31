package com.epoch.dao;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 竞赛DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "竞赛创建请求参数")
public class ContestDTO {

    @Schema(description = "竞赛标题", required = true)
    private String title;

    @Schema(description = "竞赛简介", required = true)
    private String description;

    @Schema(description = "开始时间", required = true)
    private LocalDateTime startTime;

    @Schema(description = "结束时间", required = true)
    private LocalDateTime endTime;

    @Schema(description = "竞赛规则", required = true)
    private String rules;

    @Schema(description = "状态(pending/approved/rejected)")
    private String status;

    @Schema(description = "奖项设置", required = true)
    private String awards;

    @Schema(description = "题目列表", required = true)
    private List<QuestionDTO> questions;
} 