package com.epoch.service.impl;

import com.epoch.dao.NotificationDTO;
import com.epoch.dao.NotificationQueryDTO;
import com.epoch.entity.Notification;
import com.epoch.entity.User;
import com.epoch.mapper.NotificationMapper;
import com.epoch.mapper.UserMapper;
import com.epoch.service.NotificationService;
import com.epoch.vo.NotificationPageVO;
import com.epoch.vo.NotificationResultVO;
import com.epoch.vo.NotificationVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 通知服务实现类
 */
@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;
    
    @Autowired
    private UserMapper userMapper;

    /**
     * 发送通知
     * @param notificationDTO 通知信息
     * @return 发送结果
     */
    @Override
    @Transactional
    public NotificationResultVO sendNotification(NotificationDTO notificationDTO) {
        List<Long> userIds = new ArrayList<>();
        
        // 处理目标用户
        if (notificationDTO.getTargetUsers().contains("all")) {
            // 发送给所有用户
            List<User> allUsers = userMapper.listAll();
            userIds = allUsers.stream()
                    .map(User::getId)
                    .collect(Collectors.toList());
        } else {
            // 发送给指定用户
            for (String userIdStr : notificationDTO.getTargetUsers()) {
                try {
                    Long userId = Long.parseLong(userIdStr);
                    User user = userMapper.getById(userId);
                    if (user != null) {
                        userIds.add(userId);
                    }
                } catch (NumberFormatException e) {
                    log.warn("Invalid user ID: {}", userIdStr);
                }
            }
        }
        
        if (userIds.isEmpty()) {
            return NotificationResultVO.builder()
                    .notificationId(0L)
                    .sentCount(0)
                    .build();
        }
        
        // 批量发送通知
        Long notificationId = null;
        int sentCount = 0;
        
        for (Long userId : userIds) {
            Notification notification = new Notification();
            notification.setTitle(notificationDTO.getTitle());
            notification.setContent(notificationDTO.getContent());
            notification.setType(notificationDTO.getType());
            notification.setUserId(userId);
            notification.setStatus("unread");
            
            notificationMapper.insert(notification);
            
            if (notificationId == null) {
                notificationId = notification.getId();
            }
            
            sentCount++;
        }
        
        return NotificationResultVO.builder()
                .notificationId(notificationId)
                .sentCount(sentCount)
                .build();
    }

    /**
     * 获取用户通知列表
     * @param userId 用户ID
     * @param queryDTO 查询条件
     * @return 通知列表
     */
    @Override
    public NotificationPageVO getUserNotifications(Long userId, NotificationQueryDTO queryDTO) {
        // 设置分页
        PageHelper.startPage(queryDTO.getPage(), queryDTO.getPageSize());
        
        // 查询通知列表
        List<Notification> notifications = notificationMapper.listByUserIdAndCondition(
                userId,
                queryDTO.getType(),
                queryDTO.getStatus()
        );
        
        // 转换为VO
        List<NotificationVO> notificationVOList = notifications.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        
        // 获取分页信息
        Page<Notification> page = (Page<Notification>) notifications;
        
        // 获取未读数量
        int unreadCount = notificationMapper.countUnreadByUserId(userId);
        
        return NotificationPageVO.builder()
                .total(page.getTotal())
                .unreadCount(unreadCount)
                .list(notificationVOList)
                .build();
    }

    /**
     * 标记通知为已读
     * @param notificationId 通知ID
     * @param userId 用户ID
     * @return 是否成功
     */
    @Override
    public boolean markAsRead(Long notificationId, Long userId) {
        // 获取通知
        Notification notification = notificationMapper.getById(notificationId);
        
        // 验证通知是否属于该用户
        if (notification == null || !notification.getUserId().equals(userId)) {
            return false;
        }
        
        // 已经是已读状态
        if ("read".equals(notification.getStatus())) {
            return true;
        }
        
        // 更新状态
        return notificationMapper.updateStatus(notificationId, "read") > 0;
    }

    /**
     * 标记所有通知为已读
     * @param userId 用户ID
     * @return 标记数量
     */
    @Override
    public int markAllAsRead(Long userId) {
        return notificationMapper.markAllAsRead(userId);
    }
    
    /**
     * 将实体转换为VO
     * @param notification 通知实体
     * @return 通知VO
     */
    private NotificationVO convertToVO(Notification notification) {
        NotificationVO vo = new NotificationVO();
        BeanUtils.copyProperties(notification, vo);
        vo.setNotificationId(notification.getId());
        return vo;
    }
} 