package com.epoch.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 仪表盘统计数据展示对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "仪表盘统计数据展示对象")
public class StatisticsDashboardVO {
    
    @Schema(description = "总竞赛数量")
    private Integer totalContests;
    
    @Schema(description = "总用户数量")
    private Integer totalUsers;
    
    @Schema(description = "学生数量")
    private Integer totalStudents;
    
    @Schema(description = "教师数量")
    private Integer totalTeachers;
    
    @Schema(description = "总报名数量")
    private Integer totalRegistrations;
    
    @Schema(description = "进行中的竞赛数量")
    private Integer ongoingContests;
    
    @Schema(description = "已结束的竞赛数量")
    private Integer endedContests;
    
    @Schema(description = "未开始的竞赛数量")
    private Integer pendingContests;
    
    @Schema(description = "总提交数量")
    private Integer totalSubmissions;
    
    @Schema(description = "已评分提交数量")
    private Integer scoredSubmissions;
    
    @Schema(description = "待评分提交数量")
    private Integer pendingSubmissions;
} 