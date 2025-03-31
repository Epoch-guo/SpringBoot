package com.epoch.service.impl;

import com.epoch.dao.ConfigDTO;
import com.epoch.entity.Config;
import com.epoch.mapper.ConfigMapper;
import com.epoch.service.ConfigService;
import com.epoch.vo.ConfigVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统配置服务实现类
 */
@Service
@Slf4j
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private ConfigMapper configMapper;

    /**
     * 获取配置
     * @param key 配置键
     * @return 配置值
     */
    @Override
    public ConfigVO getConfig(String key) {
        Config config = configMapper.getByKey(key);
        if (config == null) {
            return null;
        }
        
        return convertToVO(config);
    }

    /**
     * 获取所有配置
     * @return 配置列表
     */
    @Override
    public List<ConfigVO> listAllConfigs() {
        List<Config> configs = configMapper.listAll();
        return configs.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    /**
     * 更新配置
     * @param configDTO 配置信息
     * @return 是否成功
     */
    @Override
    public boolean updateConfig(ConfigDTO configDTO) {
        Config existConfig = configMapper.getByKey(configDTO.getKey());
        
        if (existConfig != null) {
            // 更新配置
            existConfig.setValue(configDTO.getValue());
            return configMapper.update(existConfig) > 0;
        } else {
            // 新增配置
            Config config = new Config();
            config.setKey(configDTO.getKey());
            config.setValue(configDTO.getValue());
            return configMapper.insert(config) > 0;
        }
    }

    /**
     * 删除配置
     * @param key 配置键
     * @return 是否成功
     */
    @Override
    public boolean deleteConfig(String key) {
        return configMapper.deleteByKey(key) > 0;
    }
    
    /**
     * 将实体转换为VO
     * @param config 配置实体
     * @return 配置VO
     */
    private ConfigVO convertToVO(Config config) {
        ConfigVO vo = new ConfigVO();
        BeanUtils.copyProperties(config, vo);
        return vo;
    }
} 