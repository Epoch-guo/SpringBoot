package com.epoch.vo;

import lombok.Data;
import java.util.Date;
import java.util.Map;

/**
 * 竞赛统计VO
 */
@Data
public class ContestStatisticsVO {
    
    private Long contestId;               // 竞赛ID
    private String title;                 // 竞赛标题
    private String status;                // 竞赛状态
    private Date startTime;               // 开始时间
    private Date endTime;                 // 结束时间
    private Long creatorId;               // 创建者ID
    private String creatorName;           // 创建者名称
    private Integer registrationCount;    // 报名人数
    private Integer submissionCount;      // 提交人数
    private Integer scoredCount;          // 已评分人数
    private Integer pendingCount;         // 待评分人数
    private Double averageScore;          // 平均分
    private Integer highestScore;         // 最高分
    private Integer lowestScore;          // 最低分
    private Map<String, Integer> scoreDistribution;  // 分数分布
    
    // 添加setter方法别名以兼容现有代码
    public void setScoredSubmissionCount(Integer scoredCount) {
        this.scoredCount = scoredCount;
    }
    
    public void setPendingSubmissionCount(Integer pendingCount) {
        this.pendingCount = pendingCount;
    }
} 