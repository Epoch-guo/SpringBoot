package com.epoch.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 系统日志实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "系统日志实体")
public class Log {
    
    @Schema(description = "日志ID")
    private Long id;
    
    @Schema(description = "用户ID")
    private Long userId;
    
    @Schema(description = "用户名")
    private String username;
    
    @Schema(description = "操作类型")
    private String actionType;
    
    @Schema(description = "IP地址")
    private String ipAddress;
    
    @Schema(description = "操作详情")
    private String details;
    
    @Schema(description = "操作时间")
    private LocalDateTime timestamp;
} 