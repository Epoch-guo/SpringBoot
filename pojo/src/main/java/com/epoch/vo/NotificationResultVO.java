package com.epoch.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通知结果VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "通知发送结果响应")
public class NotificationResultVO {
    
    @Schema(description = "通知ID")
    private Long notificationId;
    
    @Schema(description = "发送数量")
    private Integer sentCount;
} 