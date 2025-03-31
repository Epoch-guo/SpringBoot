package com.epoch.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.epoch.constant.MessageConstant;
import com.epoch.dao.ConfigDTO;
import com.epoch.dao.LogQueryDTO;
import com.epoch.result.PageResult;
import com.epoch.result.Result;
import com.epoch.service.ConfigService;
import com.epoch.service.LogService;
import com.epoch.vo.ConfigVO;
import com.epoch.vo.LogVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 系统管理控制器
 */
@RestController
@RequestMapping("/api/admin")
@Slf4j
@Tag(name = "系统管理相关接口", description = "包含系统配置、日志管理等接口")
public class SystemController {

    @Autowired
    private ConfigService configService;
    
    @Autowired
    private LogService logService;
    
    /**
     * 获取所有系统配置
     * @return 配置列表
     */
    @GetMapping("/config")
    @Operation(summary = "获取所有系统配置", description = "获取所有系统配置项")
    @SaCheckPermission("system:manage")
    public Result<List<ConfigVO>> getAllConfigs() {
        log.info("获取所有系统配置");
        List<ConfigVO> configs = configService.listAllConfigs();
        return Result.success(configs);
    }
    
    /**
     * 获取指定系统配置
     * @param key 配置键
     * @return 配置值
     */
    @GetMapping("/config/{key}")
    @Operation(summary = "获取指定系统配置", description = "根据配置键获取配置值")
    @SaCheckPermission("system:manage")
    public Result<ConfigVO> getConfig(@PathVariable @Parameter(description = "配置键") String key) {
        log.info("获取系统配置：key = {}", key);
        ConfigVO config = configService.getConfig(key);
        return Result.success(config);
    }
    
    /**
     * 更新系统配置
     * @param configDTO 配置信息
     * @return 更新结果
     */
    @PostMapping("/config")
    @Operation(summary = "更新系统配置", description = "更新系统配置项")
    @SaCheckPermission("system:manage")
    public Result<String> updateConfig(@RequestBody @Parameter(description = "配置信息") ConfigDTO configDTO) {
        log.info("更新系统配置：key = {}, value = {}", configDTO.getKey(), configDTO.getValue());
        boolean success = configService.updateConfig(configDTO);
        
        // 记录操作日志
        Long userId = StpUtil.getLoginIdAsLong();
        logService.recordLog(userId, "UPDATE_CONFIG", StpUtil.getLoginDevice(), 
                "更新配置：" + configDTO.getKey() + " = " + configDTO.getValue());
        
        return success ? 
                Result.success(null, MessageConstant.CONFIG_UPDATE_SUCCESS) : 
                Result.error(MessageConstant.CONFIG_UPDATE_FAILED);
    }
    
    /**
     * 删除系统配置
     * @param key 配置键
     * @return 删除结果
     */
    @DeleteMapping("/config/{key}")
    @Operation(summary = "删除系统配置", description = "删除指定的系统配置项")
    @SaCheckPermission("system:manage")
    public Result<String> deleteConfig(@PathVariable @Parameter(description = "配置键") String key) {
        log.info("删除系统配置：key = {}", key);
        boolean success = configService.deleteConfig(key);
        
        // 记录操作日志
        Long userId = StpUtil.getLoginIdAsLong();
        logService.recordLog(userId, "DELETE_CONFIG", StpUtil.getLoginDevice(), 
                "删除配置：" + key);
        
        return success ? 
                Result.success(null, MessageConstant.CONFIG_DELETE_SUCCESS) : 
                Result.error(MessageConstant.CONFIG_DELETE_FAILED);
    }
    
    /**
     * 查询系统日志
     * @param page 页码
     * @param pageSize 每页数量
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param actionType 操作类型
     * @return 日志列表
     */
    @GetMapping("/logs")
    @Operation(summary = "查询系统日志", description = "分页查询系统操作日志")
    @SaCheckPermission("system:manage")
    public Result<PageResult<LogVO>> getLogs(
            @RequestParam(defaultValue = "1") @Parameter(description = "页码") Integer page,
            @RequestParam(defaultValue = "10") @Parameter(description = "每页数量") Integer pageSize,
            @RequestParam(required = false) @Parameter(description = "开始时间") 
                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @Parameter(description = "结束时间") 
                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @RequestParam(required = false) @Parameter(description = "操作类型") String actionType
    ) {
        log.info("查询系统日志：page = {}, pageSize = {}, startTime = {}, endTime = {}, actionType = {}", 
                page, pageSize, startTime, endTime, actionType);
        
        LogQueryDTO logQueryDTO = LogQueryDTO.builder()
                .page(page)
                .pageSize(pageSize)
                .startTime(startTime)
                .endTime(endTime)
                .actionType(actionType)
                .build();
        
        PageResult<LogVO> pageResult = logService.queryLogs(logQueryDTO);
        return Result.success(pageResult);
    }
    
    /**
     * 清理系统日志
     * @param days 天数
     * @return 清理结果
     */
    @DeleteMapping("/logs/clean")
    @Operation(summary = "清理系统日志", description = "清理指定天数之前的系统日志")
    @SaCheckPermission("system:manage")
    public Result<String> cleanLogs(@RequestParam @Parameter(description = "天数") Integer days) {
        log.info("清理系统日志：days = {}", days);
        int count = logService.cleanLogs(days);
        
        // 记录操作日志
        Long userId = StpUtil.getLoginIdAsLong();
        logService.recordLog(userId, "CLEAN_LOGS", StpUtil.getLoginDevice(), 
                "清理" + days + "天前的日志，共" + count + "条");
        
        return Result.success(null, "成功清理" + count + "条日志");
    }
}