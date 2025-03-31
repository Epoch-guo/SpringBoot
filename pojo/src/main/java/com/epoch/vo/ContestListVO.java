package com.epoch.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 竞赛列表VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "竞赛列表项")
public class ContestListVO {
    
    @Schema(description = "竞赛ID")
    private Long contestId;
    
    @Schema(description = "竞赛标题")
    private String title;
    
    @Schema(description = "开始时间")
    private LocalDateTime startTime;
    
    @Schema(description = "结束时间")
    private LocalDateTime endTime;
    
    @Schema(description = "状态(pending/ongoing/ended)")
    private String status;
    
    @Schema(description = "报名人数")
    private Integer registrationCount;
} 