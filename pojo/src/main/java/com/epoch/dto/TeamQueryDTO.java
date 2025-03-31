package com.epoch.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 团队查询DTO
 */
@Data
@Schema(description = "团队查询DTO")
public class TeamQueryDTO {
    
    @Schema(description = "页码")
    private Integer page = 1;
    
    @Schema(description = "每页数量")
    private Integer pageSize = 10;
    
    @Schema(description = "关键词")
    private String keyword;
    
    @Schema(description = "竞赛ID")
    private Long contestId;
    
    @Schema(description = "状态")
    private String status;
} 