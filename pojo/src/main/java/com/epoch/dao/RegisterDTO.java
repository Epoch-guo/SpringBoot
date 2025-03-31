package com.epoch.dao;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 注册DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "注册请求参数")
public class RegisterDTO {
    
    @Schema(description = "姓名", required = true)
    private String username;
    
    @Schema(description = "密码", required = true)
    private String password;
    
    @Schema(description = "邮箱", required = true)
    private String email;
    
    @Schema(description = "手机号")
    private String phone;
    
    @Schema(description = "学号", required = true)
    private String studentId;
} 