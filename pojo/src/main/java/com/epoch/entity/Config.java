package com.epoch.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 系统配置实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "系统配置实体")
public class Config {
    
    @Schema(description = "配置键")
    private String key;
    
    @Schema(description = "配置值")
    private String value;
    
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
} 