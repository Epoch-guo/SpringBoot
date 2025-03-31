package com.epoch.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 竞赛VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "竞赛信息")
public class ContestVO {
    
    @Schema(description = "竞赛ID")
    private Long contestId;
    
    @Schema(description = "竞赛标题")
    private String title;
    
    @Schema(description = "竞赛简介")
    private String description;
    
    @Schema(description = "开始时间")
    private LocalDateTime startTime;
    
    @Schema(description = "结束时间")
    private LocalDateTime endTime;
    
    @Schema(description = "竞赛规则")
    private String rules;
    
    @Schema(description = "奖项设置")
    private String awards;
    
    @Schema(description = "创建者ID")
    private Long creatorId;
    
    @Schema(description = "创建者姓名")
    private String creatorName;
    
    @Schema(description = "状态(pending/ongoing/ended)")
    private String status;
    
    @Schema(description = "报名人数")
    private Integer registrationCount;
    
    @Schema(description = "创建时间")
    private LocalDateTime createdTime;
    
    @Schema(description = "题目列表")
    private List<QuestionVO> questions;
    
    @Schema(description = "报名信息")
    private ContestRegistrationsVO registrations;
} 