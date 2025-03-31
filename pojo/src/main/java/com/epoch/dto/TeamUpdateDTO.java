package com.epoch.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.Size;

/**
 * 团队更新DTO
 */
@Data
@Schema(description = "团队更新DTO")
public class TeamUpdateDTO {

    @Schema(description = "团队ID", required = true)
    private Long id;

    @Size(max = 50, message = "团队名称长度不能超过50个字符")
    @Schema(description = "团队名称")
    private String teamName;

    @Size(max = 500, message = "团队描述长度不能超过500个字符")
    @Schema(description = "团队描述")
    private String teamDescription;

    @Schema(description = "最大成员数")
    private Integer maxMembers;
    @Schema(description = "状态(pending/approved/rejected)")
    private String status;

    @Schema(description = "队长ID")
    private Long leaderId;
} 