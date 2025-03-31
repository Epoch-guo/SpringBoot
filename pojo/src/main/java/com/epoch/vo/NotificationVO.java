package com.epoch.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 通知VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "通知响应")
public class NotificationVO {
    
    @Schema(description = "通知ID")
    private Long notificationId;
    
    @Schema(description = "标题")
    private String title;
    
    @Schema(description = "通知内容")
    private String content;
    
    @Schema(description = "通知类型(announcement/reminder/audit_result)")
    private String type;
    
    @Schema(description = "阅读状态(unread/read)")
    private String status;
    
    @Schema(description = "创建时间")
    private LocalDateTime createdTime;
} 