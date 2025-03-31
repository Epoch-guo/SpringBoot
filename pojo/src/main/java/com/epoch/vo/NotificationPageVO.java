package com.epoch.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 通知分页VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "通知分页响应")
public class NotificationPageVO {
    
    @Schema(description = "总记录数")
    private Long total;
    
    @Schema(description = "未读数量")
    private Integer unreadCount;
    
    @Schema(description = "通知列表")
    private List<NotificationVO> list;
} 