package com.epoch.config;

import cn.dev33.satoken.stp.StpInterface;
import com.epoch.entity.User;
import com.epoch.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义权限验证接口扩展
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    @Autowired
    private UserMapper userMapper;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 获取用户ID
        Long userId = Long.parseLong(loginId.toString());
        
        // 根据用户ID获取用户信息
        User user = userMapper.getById(userId);
        if (user == null) {
            return new ArrayList<>();
        }
        
        // 根据角色返回权限列表
        List<String> permissions = new ArrayList<>();
        
        // 根据角色设置不同的权限
        switch (user.getRole()) {
            case "admin":
                // 管理员拥有所有权限
                permissions.add("*"); // 添加通配符权限，表示拥有所有权限
                
                // 也可以明确列出所有权限，以便于日志记录和调试
                permissions.add("user:manage");
                permissions.add("contest:manage");
                permissions.add("system:manage");
                permissions.add("contest:view");
                permissions.add("contest:create");
                permissions.add("contest:edit");
                permissions.add("submission:review");
                permissions.add("submission:create");
                break;
            case "teacher":
                permissions.add("contest:create");
                permissions.add("contest:edit");
                permissions.add("contest:view");
                permissions.add("submission:review");
                break;
            case "student":
                permissions.add("contest:view");
                permissions.add("submission:create");
                break;
        }
        
        return permissions;
    }

    /**
     * 返回一个账号所拥有的角色标识集合
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 获取用户ID
        Long userId = Long.parseLong(loginId.toString());
        
        // 根据用户ID获取用户信息
        User user = userMapper.getById(userId);
        if (user == null) {
            return new ArrayList<>();
        }
        
        // 返回角色列表
        List<String> roles = new ArrayList<>();
        roles.add(user.getRole());
        
        // 如果是admin，也可以添加所有角色
        if ("admin".equals(user.getRole())) {
            roles.add("teacher");
            roles.add("student");
        }
        
        return roles;
    }
} 