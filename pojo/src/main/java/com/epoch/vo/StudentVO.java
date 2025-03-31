package com.epoch.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 学生VO
 */
@Data
@Schema(description = "学生信息")
public class StudentVO {
    
    @Schema(description = "用户ID")
    private Long id;
    
    @Schema(description = "学号/用户名")
    private String username;
    
    @Schema(description = "邮箱")
    private String email;
    
    @Schema(description = "手机号")
    private String phone;
    
    @Schema(description = "是否为队长")
    private Boolean isLeader;
} 