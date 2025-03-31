package com.epoch.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.epoch.constant.MessageConstant;
import com.epoch.dao.NotificationDTO;
import com.epoch.result.Result;
import com.epoch.service.LogService;
import com.epoch.service.NotificationService;
import com.epoch.vo.NotificationResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 通知管理控制器（管理员）
 */
@RestController
@RequestMapping("/api/admin/notifications")
@Slf4j
@Tag(name = "通知管理相关接口（管理员）", description = "包含发送通知等接口")
public class NotificationAdminController {

    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private LogService logService;
    
    /**
     * 发送通知
     * @param notificationDTO 通知信息
     * @return 发送结果
     */
    @PostMapping
    @Operation(summary = "发送通知", description = "发送系统通知")
    @SaCheckPermission("system:manage")
    public Result<NotificationResultVO> sendNotification(@RequestBody @Parameter(description = "通知信息") NotificationDTO notificationDTO) {
        log.info("发送通知：title = {}, type = {}, targetUsers = {}", 
                notificationDTO.getTitle(), notificationDTO.getType(), notificationDTO.getTargetUsers());
        
        NotificationResultVO result = notificationService.sendNotification(notificationDTO);
        
        // 记录操作日志
        Long userId = StpUtil.getLoginIdAsLong();
        logService.recordLog(userId, "SEND_NOTIFICATION", StpUtil.getLoginDevice(), 
                "发送通知：" + notificationDTO.getTitle() + "，发送数量：" + result.getSentCount());
        
        return Result.success(result, "发送成功");
    }
} 