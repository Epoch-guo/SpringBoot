package com.epoch.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 用户分页VO
 */
@Data
@Schema(description = "用户分页VO")
public class UserPageVO {
    
    @Schema(description = "总记录数")
    private Long total;
    
    @Schema(description = "用户列表")
    private List<UserVO> list;
} 