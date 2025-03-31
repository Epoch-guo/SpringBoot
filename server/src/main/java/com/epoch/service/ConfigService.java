package com.epoch.service;

import com.epoch.dao.ConfigDTO;
import com.epoch.vo.ConfigVO;

import java.util.List;

/**
 * 系统配置服务接口
 */
public interface ConfigService {
    
    /**
     * 获取配置
     * @param key 配置键
     * @return 配置值
     */
    ConfigVO getConfig(String key);
    
    /**
     * 获取所有配置
     * @return 配置列表
     */
    List<ConfigVO> listAllConfigs();
    
    /**
     * 更新配置
     * @param configDTO 配置信息
     * @return 是否成功
     */
    boolean updateConfig(ConfigDTO configDTO);
    
    /**
     * 删除配置
     * @param key 配置键
     * @return 是否成功
     */
    boolean deleteConfig(String key);
} 