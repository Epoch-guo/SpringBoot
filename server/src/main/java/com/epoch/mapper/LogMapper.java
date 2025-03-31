package com.epoch.mapper;

import com.epoch.entity.Log;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 系统日志Mapper接口
 */
@Mapper
public interface LogMapper {
    
    /**
     * 根据ID获取日志
     * @param id 日志ID
     * @return 日志对象
     */
    Log getById(Long id);
    
    /**
     * 保存日志
     * @param log 日志对象
     * @return 影响行数
     */
    int insert(Log log);
    
    /**
     * 条件查询日志列表
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param actionType 操作类型
     * @return 日志列表
     */
    List<Log> listByCondition(@Param("startTime") LocalDateTime startTime,
                              @Param("endTime") LocalDateTime endTime,
                              @Param("actionType") String actionType);
    
    /**
     * 删除指定时间之前的日志
     * @param time 时间点
     * @return 影响行数
     */
    int deleteBeforeTime(LocalDateTime time);
} 