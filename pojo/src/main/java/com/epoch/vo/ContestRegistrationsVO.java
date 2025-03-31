package com.epoch.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 竞赛报名信息VO
 */
@Data
@Schema(description = "竞赛报名信息")
public class ContestRegistrationsVO {
    
    @Schema(description = "报名总数")
    private Integer total;
    
    @Schema(description = "团队报名列表")
    private List<TeamVO> teams;
    
    @Schema(description = "个人报名列表")
    private List<RegistrationVO> individuals;
} 