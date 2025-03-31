package com.epoch.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.epoch.dao.NotificationQueryDTO;
import com.epoch.result.Result;
import com.epoch.service.NotificationService;
import com.epoch.vo.NotificationPageVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 通知控制器（用户）
 */
@RestController
@RequestMapping("/api/notifications")
@Slf4j
@Tag(name = "通知相关接口（用户）", description = "包含获取通知列表、标记已读等接口")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;
    
    /**
     * 获取通知列表
     * @param page 页码
     * @param pageSize 每页数量
     * @param type 通知类型
     * @param status 读取状态
     * @return 通知列表
     */
    @GetMapping
    @Operation(summary = "获取通知列表", description = "获取用户通知列表")
    @SaCheckLogin
    public Result<NotificationPageVO> getNotifications(
            @RequestParam(defaultValue = "1") @Parameter(description = "页码") Integer page,
            @RequestParam(defaultValue = "10") @Parameter(description = "每页数量") Integer pageSize,
            @RequestParam(required = false) @Parameter(description = "通知类型") String type,
            @RequestParam(required = false) @Parameter(description = "读取状态") String status
    ) {
        log.info("获取通知列表：page = {}, pageSize = {}, type = {}, status = {}", 
                page, pageSize, type, status);
        
        Long userId = StpUtil.getLoginIdAsLong();
        
        NotificationQueryDTO queryDTO = NotificationQueryDTO.builder()
                .page(page)
                .pageSize(pageSize)
                .type(type)
                .status(status)
                .build();
        
        NotificationPageVO pageVO = notificationService.getUserNotifications(userId, queryDTO);
        return Result.success(pageVO);
    }
    
    /**
     * 标记通知已读
     * @param id 通知ID
     * @return 标记结果
     */
    @PutMapping("/{id}/read")
    @Operation(summary = "标记通知已读", description = "标记指定通知为已读状态")
    @SaCheckLogin
    public Result<String> markAsRead(@PathVariable @Parameter(description = "通知ID") Long id) {
        log.info("标记通知已读：id = {}", id);
        
        Long userId = StpUtil.getLoginIdAsLong();
        boolean success = notificationService.markAsRead(id, userId);
        
        return success ? 
                Result.success(null, "标记成功") : 
                Result.error("标记失败，通知不存在或不属于当前用户");
    }
    
    /**
     * 标记所有通知已读
     * @return 标记结果
     */
    @PutMapping("/read-all")
    @Operation(summary = "标记所有通知已读", description = "标记用户所有通知为已读状态")
    @SaCheckLogin
    public Result<String> markAllAsRead() {
        log.info("标记所有通知已读");
        
        Long userId = StpUtil.getLoginIdAsLong();
        int count = notificationService.markAllAsRead(userId);
        
        return Result.success(null, "成功标记" + count + "条通知为已读");
    }
} 