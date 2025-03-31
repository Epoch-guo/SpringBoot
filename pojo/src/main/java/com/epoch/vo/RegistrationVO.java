package com.epoch.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 报名VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "报名信息")
public class RegistrationVO {
    
    @Schema(description = "报名ID")
    private Long registrationId;
    
    @Schema(description = "用户ID")
    private Long userId;
    
    @Schema(description = "用户名")
    private String username;
    
    @Schema(description = "状态(pending/approved/rejected)")
    private String status;
    
    @Schema(description = "竞赛ID")
    private Long contestId;
    
    @Schema(description = "竞赛标题")
    private String contestTitle;
    
    @Schema(description = "队长名称")
    private String leaderName;
    
    @Schema(description = "团队名称")
    private String teamName;
    
    @Schema(description = "团队成员")
    private List<TeamMemberVO> members;
    
    @Schema(description = "附件路径")
    private String attachmentPath;
    
    @Schema(description = "创建时间")
    private LocalDateTime createdTime;
} 