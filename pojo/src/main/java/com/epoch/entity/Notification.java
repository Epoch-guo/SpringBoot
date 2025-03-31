package com.epoch.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 通知实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "通知实体")
public class Notification {
    
    @Schema(description = "通知ID")
    private Long id;
    
    @Schema(description = "标题")
    private String title;
    
    @Schema(description = "接收用户ID")
    private Long userId;
    
    @Schema(description = "通知内容")
    private String content;
    
    @Schema(description = "通知类型(announcement/reminder/audit_result)")
    private String type;
    
    @Schema(description = "阅读状态(unread/read)")
    private String status;
    
    @Schema(description = "创建时间")
    private LocalDateTime createdTime;
} 