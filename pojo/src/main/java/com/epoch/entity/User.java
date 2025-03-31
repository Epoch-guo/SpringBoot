package com.epoch.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户实体")
public class User {
    
    @Schema(description = "用户ID")
    private Long id;
    
    @Schema(description = "用户名/学号/工号")
    private String username;
    
    @Schema(description = "密码")
    private String password;
    
    @Schema(description = "邮箱")
    private String email;
    
    @Schema(description = "手机号")
    private String phone;
    
    @Schema(description = "用户角色(student/teacher/admin)")
    private String role;
    
    @Schema(description = "状态(active/inactive)")
    private String status;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
} 