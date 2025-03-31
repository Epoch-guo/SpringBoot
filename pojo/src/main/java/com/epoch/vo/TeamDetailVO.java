package com.epoch.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 团队详情VO
 */
@Data
@Schema(description = "团队详情VO")
public class TeamDetailVO {
    
    @Schema(description = "团队ID")
    private Long teamId;
    
    @Schema(description = "团队名称")
    private String teamName;
    
    @Schema(description = "团队描述")
    private String teamDescription;
    
    @Schema(description = "最大成员数")
    private Integer maxMembers;
    
    @Schema(description = "竞赛ID")
    private Long contestId;
    
    @Schema(description = "竞赛名称")
    private String contestName;
    
    @Schema(description = "队长信息")
    private UserVO leader;
    
    @Schema(description = "成员列表")
    private List<UserVO> members;
    
    @Schema(description = "状态")
    private String status;
    
    @Schema(description = "创建时间")
    private LocalDateTime createdTime;
} 