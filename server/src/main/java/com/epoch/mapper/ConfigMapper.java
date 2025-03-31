package com.epoch.mapper;

import com.epoch.entity.Config;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统配置Mapper接口
 */
@Mapper
public interface ConfigMapper {
    
    /**
     * 根据配置键获取配置
     * @param key 配置键
     * @return 配置对象
     */
    Config getByKey(String key);
    
    /**
     * 获取所有配置
     * @return 配置列表
     */
    List<Config> listAll();
    
    /**
     * 保存配置
     * @param config 配置对象
     * @return 影响行数
     */
    int insert(Config config);
    
    /**
     * 更新配置
     * @param config 配置对象
     * @return 影响行数
     */
    int update(Config config);
    
    /**
     * 删除配置
     * @param key 配置键
     * @return 影响行数
     */
    int deleteByKey(String key);
} 