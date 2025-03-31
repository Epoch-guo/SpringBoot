package com.epoch.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.epoch.constant.MessageConstant;
import com.epoch.dao.UserQueryDTO;
import com.epoch.dao.UserUpdateDTO;
import com.epoch.result.Result;
import com.epoch.service.UserService;
import com.epoch.vo.UserPageVO;
import com.epoch.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户管理控制器
 */
@RestController
@RequestMapping("/api/admin/users")
@Slf4j
@Tag(name = "用户管理接口", description = "包含用户查询、更新、删除等接口")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 分页查询用户列表
     * @param queryDTO 查询条件
     * @return 用户分页列表
     */
    @GetMapping
    @Operation(summary = "分页查询用户列表", description = "根据条件分页查询用户列表")
    @SaCheckPermission("user:manage")
    public Result<UserPageVO> pageQuery(UserQueryDTO queryDTO) {
        UserPageVO pageVO = userService.pageQuery(queryDTO);
        return Result.success(pageVO);
    }

    /**
     * 获取用户详情
     * @param id 用户ID
     * @return 用户详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取用户详情", description = "根据ID获取用户详情")
    @SaCheckPermission("user:manage")
    public Result<UserVO> getUserDetail(@PathVariable @Parameter(description = "用户ID") Long id) {
        UserVO userVO = userService.getUserDetail(id);
        return Result.success(userVO);
    }

    /**
     * 更新用户信息
     * @param updateDTO 更新信息
     * @return 更新结果
     */
    @PutMapping
    @Operation(summary = "更新用户信息", description = "更新用户基本信息、角色和状态")
    @SaCheckPermission("user:manage")
    public Result<Void> updateUser(@RequestBody @Valid UserUpdateDTO updateDTO) {
        boolean success = userService.updateUser(updateDTO);
        if (success) {
            return Result.success(null, MessageConstant.USER_UPDATE_SUCCESS);
        } else {
            return Result.error(MessageConstant.USER_UPDATE_FAILED);
        }
    }

    /**
     * 删除用户
     * @param id 用户ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户", description = "根据ID删除用户")
    @SaCheckPermission("user:manage")
    public Result<Void> deleteUser(@PathVariable @Parameter(description = "用户ID") Long id) {
        boolean success = userService.deleteUser(id);
        if (success) {
            return Result.success(null, MessageConstant.USER_DELETE_SUCCESS);
        } else {
            return Result.error(MessageConstant.USER_DELETE_FAILED);
        }
    }

    /**
     * 重置用户密码
     * @param id 用户ID
     * @return 新密码
     */
    @PostMapping("/{id}/reset-password")
    @Operation(summary = "重置用户密码", description = "重置用户密码并返回新密码")
    @SaCheckPermission("user:manage")
    public Result<Map<String, String>> resetPassword(@PathVariable @Parameter(description = "用户ID") Long id) {
        String newPassword = userService.resetPassword(id);
        
        Map<String, String> data = new HashMap<>();
        data.put("newPassword", newPassword);
        
        return Result.success(data, MessageConstant.PASSWORD_RESET_SUCCESS);
    }
} 