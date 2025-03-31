package com.epoch.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 登录响应VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "登录响应数据")
public class LoginVO {
    
    @Schema(description = "用户ID")
    private Long userId;
    
    @Schema(description = "用户名")
    private String username;
    
    @Schema(description = "用户角色(student/teacher/admin)")
    private String role;
    
    @Schema(description = "令牌")
    private String token;
    
    @Schema(description = "权限列表")
    private List<String> permissions;
} 