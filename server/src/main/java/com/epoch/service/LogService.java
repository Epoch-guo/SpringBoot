package com.epoch.service;

import com.epoch.dao.LogQueryDTO;
import com.epoch.entity.Log;
import com.epoch.result.PageResult;
import com.epoch.vo.LogVO;

/**
 * 系统日志服务接口
 */
public interface LogService {
    
    /**
     * 记录日志
     * @param userId 用户ID
     * @param actionType 操作类型
     * @param ipAddress IP地址
     * @param details 操作详情
     * @return 是否成功
     */
    boolean recordLog(Long userId, String actionType, String ipAddress, String details);
    
    /**
     * 分页查询日志
     * @param logQueryDTO 查询条件
     * @return 分页结果
     */
    PageResult<LogVO> queryLogs(LogQueryDTO logQueryDTO);
    
    /**
     * 清理指定天数之前的日志
     * @param days 天数
     * @return 清理数量
     */
    int cleanLogs(int days);
} 