package com.epoch.dao;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通知查询DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "通知查询请求")
public class NotificationQueryDTO {
    
    @Schema(description = "页码")
    @Builder.Default
    private Integer page = 1;
    
    @Schema(description = "每页数量")
    @Builder.Default
    private Integer pageSize = 10;
    
    @Schema(description = "通知类型(announcement/reminder/audit_result)")
    private String type;
    
    @Schema(description = "阅读状态(read/unread)")
    private String status;
} 