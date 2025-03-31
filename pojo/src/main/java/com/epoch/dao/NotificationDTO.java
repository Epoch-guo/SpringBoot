package com.epoch.dao;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 通知DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "通知请求")
public class NotificationDTO {
    
    @Schema(description = "标题", required = true)
    private String title;
    
    @Schema(description = "内容", required = true)
    private String content;
    
    @Schema(description = "类型(announcement/reminder/audit_result)", required = true)
    private String type;
    
    @Schema(description = "目标用户(all或用户ID列表)", required = true)
    private List<String> targetUsers;
} 