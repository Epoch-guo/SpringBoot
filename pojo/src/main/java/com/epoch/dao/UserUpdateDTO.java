package com.epoch.dao;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 用户更新DTO
 */
@Data
@Schema(description = "用户更新DTO")
public class UserUpdateDTO {
    
    @Schema(description = "用户ID", required = true)
    private Long id;
    
    @Size(max = 50, message = "用户名长度不能超过50个字符")
    @Schema(description = "用户名/学号/工号")
    private String username;
    
    @Email(message = "邮箱格式不正确")
    @Schema(description = "邮箱")
    private String email;
    
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Schema(description = "手机号")
    private String phone;
    
    @Schema(description = "用户角色(student/teacher/admin)")
    private String role;
    
    @Schema(description = "状态(active/inactive)")
    private String status;
} 