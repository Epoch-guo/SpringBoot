package com.epoch.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * 日志配置类
 */
@Configuration
public class LoggingConfig {

    /**
     * 获取指定类的日志记录器
     * @param clazz 类
     * @return 日志记录器
     */
    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }
} 