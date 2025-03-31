package com.epoch.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 系统配置VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "系统配置响应")
public class ConfigVO {
    
    @Schema(description = "配置项键")
    private String key;
    
    @Schema(description = "配置项值")
    private String value;
    
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
} 