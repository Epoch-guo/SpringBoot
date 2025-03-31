package com.epoch.utils;

import cn.dev33.satoken.stp.StpUtil;
import com.epoch.entity.User;
import org.springframework.stereotype.Component;

/**
 * 安全工具类，用于获取当前登录用户信息
 */
@Component
public class SecurityUtils {

    private static Object userMapper;

    @org.springframework.beans.factory.annotation.Autowired
    public void setUserMapper(@org.springframework.beans.factory.annotation.Qualifier("userMapper") Object userMapper) {
        SecurityUtils.userMapper = userMapper;
    }

    /**
     * 获取当前登录用户ID
     * @return 用户ID
     */
    public static Long getCurrentUserId() {
        return StpUtil.getLoginIdAsLong();
    }

    /**
     * 获取当前登录用户
     * @return 用户对象
     */
    public static User getCurrentUser() {
        // 暂时直接返回null，实际实现将在server模块中
        return null;
    }

    /**
     * 判断当前用户是否为管理员
     * @return 是否为管理员
     */
    public static boolean isAdmin() {
        User user = getCurrentUser();
        return user != null && "admin".equals(user.getRole());
    }

    /**
     * 判断当前用户是否为教师
     * @return 是否为教师
     */
    public static boolean isTeacher() {
        User user = getCurrentUser();
        return user != null && "teacher".equals(user.getRole());
    }

    /**
     * 判断当前用户是否为学生
     * @return 是否为学生
     */
    public static boolean isStudent() {
        User user = getCurrentUser();
        return user != null && "student".equals(user.getRole());
    }
} 