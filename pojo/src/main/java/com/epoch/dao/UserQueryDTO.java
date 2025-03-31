package com.epoch.dao;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户查询DTO
 */
@Data
@Schema(description = "用户查询DTO")
public class UserQueryDTO {
    
    @Schema(description = "页码", defaultValue = "1")
    private Integer page = 1;
    
    @Schema(description = "每页数量", defaultValue = "10")
    private Integer pageSize = 10;
    
    @Schema(description = "关键词（用户名/邮箱/手机号）")
    private String keyword;
    
    @Schema(description = "角色(student/teacher/admin)")
    private String role;
    
    @Schema(description = "状态(active/inactive)")
    private String status;
} 