package com.epoch.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 团队VO
 */
@Data
@Schema(description = "团队VO")
public class TeamVO {
    
    @Schema(description = "团队ID")
    private Long teamId;
    
    @Schema(description = "团队名称")
    private String teamName;
    
    @Schema(description = "队长ID")
    private Long leaderId;
    
    @Schema(description = "队长名称")
    private String leaderName;
    
    @Schema(description = "竞赛ID")
    private Long contestId;
    
    @Schema(description = "竞赛名称")
    private String contestName;
    
    @Schema(description = "成员数量")
    private Integer memberCount;
    
    @Schema(description = "状态" )
    private String status;
    
    @Schema(description = "创建时间")
    private LocalDateTime createdTime;
} 