package com.epoch.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 报名实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "报名实体")
public class Registration {
    
    @Schema(description = "报名ID")
    private Long id;
    
    @Schema(description = "竞赛ID")
    private Long contestId;
    
    @Schema(description = "用户ID（队长）")
    private Long userId;
    
    @Schema(description = "是否为团队")
    private Boolean isTeam;
    
    @Schema(description = "团队名称")
    private String teamName;
    
    @Schema(description = "团队描述")
    private String teamDescription;
    
    @Schema(description = "最大成员数")
    private Integer maxMembers;
    
    @Schema(description = "附件路径")
    private String attachmentPath;
    
    @Schema(description = "状态(pending/approved/rejected) enum('pending', 'ongoing', 'ended')")
    private String status;
    
    @Schema(description = "报名时间")
    private LocalDateTime createdTime;
} 