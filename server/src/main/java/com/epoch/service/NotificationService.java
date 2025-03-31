package com.epoch.service;

import com.epoch.dao.NotificationDTO;
import com.epoch.dao.NotificationQueryDTO;
import com.epoch.vo.NotificationPageVO;
import com.epoch.vo.NotificationResultVO;

/**
 * 通知服务接口
 */
public interface NotificationService {
    
    /**
     * 发送通知
     * @param notificationDTO 通知信息
     * @return 发送结果
     */
    NotificationResultVO sendNotification(NotificationDTO notificationDTO);
    
    /**
     * 获取用户通知列表
     * @param userId 用户ID
     * @param queryDTO 查询条件
     * @return 通知列表
     */
    NotificationPageVO getUserNotifications(Long userId, NotificationQueryDTO queryDTO);
    
    /**
     * 标记通知为已读
     * @param notificationId 通知ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean markAsRead(Long notificationId, Long userId);
    
    /**
     * 标记所有通知为已读
     * @param userId 用户ID
     * @return 标记数量
     */
    int markAllAsRead(Long userId);
} 