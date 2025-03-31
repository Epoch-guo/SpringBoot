package com.epoch.dao;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 报名DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "竞赛报名请求参数")
public class RegistrationDTO {
    
    @Schema(description = "团队名称")
    private String teamName;
    
    @Schema(description = "团队成员ID列表")
    private List<Long> members;
} 