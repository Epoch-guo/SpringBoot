package com.epoch.vo;

import lombok.Data;
import java.util.Map;

/**
 * 统计仪表盘VO
 */
@Data
public class StatisticsDashboardVO {
    
    private Integer totalUsers;           // 总用户数
    private Integer totalStudents;        // 学生数量
    private Integer totalTeachers;        // 教师数量
    private Integer totalContests;        // 竞赛总数
    private Integer ongoingContests;      // 进行中竞赛数
    private Integer endedContests;        // 已结束竞赛数
    private Integer pendingContests;      // 待开始竞赛数
    private Integer totalRegistrations;   // 总报名数
    private Integer totalSubmissions;     // 总提交数
    private Integer scoredSubmissions;    // 已评分提交数
    private Integer pendingSubmissions;   // 待评分提交数
    private Map<String, Object> scoreStatistics;  // 评分统计数据
} 