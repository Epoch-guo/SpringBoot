package com.epoch.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 竞赛实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "竞赛实体")
public class Contest {
    
    @Schema(description = "竞赛ID")
    private Long id;
    
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
    
    @Schema(description = "创建者ID（教师）")
    private Long creatorId;
    
    @Schema(description = "状态(pending/ongoing/ended)")
    private String status;
    
    @Schema(description = "创建时间")
    private LocalDateTime createdTime;
} 