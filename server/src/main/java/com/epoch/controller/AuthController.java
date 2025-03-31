package com.epoch.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.epoch.config.LoggingConfig;
import com.epoch.constant.MessageConstant;
import com.epoch.dao.LoginDTO;
import com.epoch.dao.RegisterDTO;
import com.epoch.result.Result;
import com.epoch.service.UserService;
import com.epoch.vo.LoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/api")
@Tag(name = "认证相关接口", description = "包含登录、注册等接口")
public class AuthController {

    private static final Logger log = LoggingConfig.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     * @param loginDTO 登录信息
     * @return 登录响应
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录接口，支持学生、教师和管理员角色登录，返回令牌")
    public Result<LoginVO> login(@RequestBody @Parameter(description = "登录信息") LoginDTO loginDTO) {
        log.info("用户登录：{}", loginDTO.getUsername());
        LoginVO loginVO = userService.login(loginDTO);
        return Result.success(loginVO, MessageConstant.SUCCESSLOGIN);
    }
    
    /**
     * 用户退出登录
     * @return 退出结果
     */
    @PostMapping("/logout")
    @SaCheckLogin
    @Operation(summary = "用户退出登录", description = "用户退出登录接口，清除登录状态和令牌")
    public Result<String> logout() {
        boolean success = userService.logout();
        if (success) {
            return Result.success(null, MessageConstant.LOGOUT_SUCCESS);
        } else {
            return Result.error(MessageConstant.LOGOUT_FAILED);
        }
    }

    /**
     * 学生注册
     * @param registerDTO 注册信息
     * @return 注册结果
     */
    @PostMapping("/register")
    @Operation(summary = "学生注册", description = "学生用户注册接口，需要验证学号的有效性")
    public Result<Map<String, Object>> register(@RequestBody @Parameter(description = "注册信息") RegisterDTO registerDTO) {
        log.info("学生注册：{}", registerDTO.getUsername());
        Long userId = userService.register(registerDTO);
        
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("username", registerDTO.getUsername());
        
        return Result.success(data, "注册成功");
    }
} 