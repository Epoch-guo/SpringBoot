package com.epoch.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 团队成员添加DTO
 */
@Data
@Schema(description = "团队成员添加DTO")
public class TeamMemberAddDTO {
    
    @Schema(description = "团队ID", required = true)
    private Long teamId;
    
    @NotEmpty(message = "成员ID列表不能为空")
    @Schema(description = "成员ID列表", required = true)
    private List<Long> memberIds;
} 