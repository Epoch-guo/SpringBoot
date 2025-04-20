package com.epoch.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 评分VO
 */
@Data
public class ScoreVO {
    
    private Long id;                  // 评分ID
    private Long submissionId;        // 提交ID
    private Long teacherId;           // 教师ID
    private String teacherName;       // 教师姓名
    private Integer score;            // 分数
    private String comment;           // 评语
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
    private Long contestId;           // 竞赛ID
    private String contestTitle;      // 竞赛标题
    private Long studentId;           // 学生ID
    private String studentName;       // 学生姓名
    
    // 兼容方法
    public void setScoreId(Long id) {
        this.id = id;
    }
} 