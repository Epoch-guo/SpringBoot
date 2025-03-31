package com.epoch.service.impl;

import com.epoch.dao.LogQueryDTO;
import com.epoch.entity.Log;
import com.epoch.entity.User;
import com.epoch.mapper.LogMapper;
import com.epoch.mapper.UserMapper;
import com.epoch.result.PageResult;
import com.epoch.service.LogService;
import com.epoch.vo.LogVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统日志服务实现类
 */
@Service
@Slf4j
public class LogServiceImpl implements LogService {

    @Autowired
    private LogMapper logMapper;
    
    @Autowired
    private UserMapper userMapper;

    /**
     * 记录日志
     * @param userId 用户ID
     * @param actionType 操作类型
     * @param ipAddress IP地址
     * @param details 操作详情
     * @return 是否成功
     */
    @Override
    public boolean recordLog(Long userId, String actionType, String ipAddress, String details) {
        Log log = new Log();
        log.setUserId(userId);
        log.setActionType(actionType);
        log.setIpAddress(ipAddress);
        log.setDetails(details);
        log.setTimestamp(LocalDateTime.now());
        
        return logMapper.insert(log) > 0;
    }

    /**
     * 分页查询日志
     * @param logQueryDTO 查询条件
     * @return 分页结果
     */
    @Override
    public PageResult<LogVO> queryLogs(LogQueryDTO logQueryDTO) {
        PageHelper.startPage(logQueryDTO.getPage(), logQueryDTO.getPageSize());
        
        List<Log> logs = logMapper.listByCondition(
                logQueryDTO.getStartTime(),
                logQueryDTO.getEndTime(),
                logQueryDTO.getActionType()
        );
        
        // 转换为VO
        List<LogVO> logVOList = logs.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        
        Page<Log> page = (Page<Log>) logs;
        
        return new PageResult<>(page.getTotal(), logVOList);
    }

    /**
     * 清理指定天数之前的日志
     * @param days 天数
     * @return 清理数量
     */
    @Override
    public int cleanLogs(int days) {
        LocalDateTime time = LocalDateTime.now().minusDays(days);
        return logMapper.deleteBeforeTime(time);
    }
    
    /**
     * 将实体转换为VO
     * @param log 日志实体
     * @return 日志VO
     */
    private LogVO convertToVO(Log log) {
        LogVO vo = new LogVO();
        BeanUtils.copyProperties(log, vo);
        vo.setLogId(log.getId());
        
        // 获取用户名
        User user = userMapper.getById(log.getUserId());
        if (user != null) {
            vo.setUsername(user.getUsername());
        }
        
        return vo;
    }
} 