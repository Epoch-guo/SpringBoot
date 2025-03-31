package com.epoch.dao;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "登录请求参数")
public class LoginDTO {
    
    @Schema(description = "用户名/学号", required = true)
    private String username;
    
    @Schema(description = "密码", required = true)
    private String password;
} 