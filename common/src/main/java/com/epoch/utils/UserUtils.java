package com.epoch.utils;

import cn.dev33.satoken.stp.StpUtil;

/**
 * 用户工具类
 */
public class UserUtils {
    
    /**
     * 获取当前登录用户ID
     * @return 用户ID
     */
    public static Long getCurrentUserId() {
        return Long.valueOf(StpUtil.getLoginId().toString());
    }
    
    /**
     * 判断当前用户是否为教师
     * @return 是否为教师
     */
    public static boolean isTeacher() {
        return StpUtil.hasRole("teacher");
    }
    
    /**
     * 判断当前用户是否为学生
     * @return 是否为学生
     */
    public static boolean isStudent() {
        return StpUtil.hasRole("student");
    }
    
    /**
     * 判断当前用户是否为管理员
     * @return 是否为管理员
     */
    public static boolean isAdmin() {
        return StpUtil.hasRole("admin");
    }
} 