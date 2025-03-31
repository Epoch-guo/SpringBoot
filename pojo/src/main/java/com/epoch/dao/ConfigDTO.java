package com.epoch.dao;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统配置DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "系统配置请求")
public class ConfigDTO {
    
    @Schema(description = "配置项键", required = true)
    private String key;
    
    @Schema(description = "配置项值", required = true)
    private String value;
} 