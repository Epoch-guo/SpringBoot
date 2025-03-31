package com.epoch.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 团队分页VO
 */
@Data
@Schema(description = "团队分页VO")
public class TeamPageVO {
    
    @Schema(description = "总数")
    private Long total;
    
    @Schema(description = "团队列表")
    private List<TeamVO> list;
} 