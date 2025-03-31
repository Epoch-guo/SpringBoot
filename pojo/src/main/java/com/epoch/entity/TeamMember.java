package com.epoch.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 团队成员实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "团队成员实体")
public class TeamMember {
    
    @Schema(description = "报名ID")
    private Long registrationId;
    
    @Schema(description = "成员用户ID")
    private Long memberUserId;
} 