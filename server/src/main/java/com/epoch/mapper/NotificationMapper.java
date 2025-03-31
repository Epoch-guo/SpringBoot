package com.epoch.mapper;

import com.epoch.entity.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 通知Mapper接口
 */
@Mapper
public interface NotificationMapper {
    
    /**
     * 根据ID获取通知
     * @param id 通知ID
     * @return 通知对象
     */
    Notification getById(Long id);
    
    /**
     * 保存通知
     * @param notification 通知对象
     * @return 影响行数
     */
    int insert(Notification notification);
    
    /**
     * 更新通知状态
     * @param id 通知ID
     * @param status 状态
     * @return 影响行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") String status);
    
    /**
     * 根据用户ID和条件查询通知列表
     * @param userId 用户ID
     * @param type 通知类型
     * @param status 阅读状态
     * @return 通知列表
     */
    List<Notification> listByUserIdAndCondition(
            @Param("userId") Long userId,
            @Param("type") String type,
            @Param("status") String status);
    
    /**
     * 获取用户未读通知数量
     * @param userId 用户ID
     * @return 未读数量
     */
    int countUnreadByUserId(Long userId);
    
    /**
     * 批量标记通知为已读
     * @param userId 用户ID
     * @return 影响行数
     */
    int markAllAsRead(Long userId);
} 