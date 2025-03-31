package com.epoch.vo;

import lombok.Data;
import java.util.Date;

/**
 * 评分排名VO
 */
@Data
public class ScoreRankingVO {
    
    private Integer rank;              // 排名
    private Long studentId;            // 学生ID
    private String studentName;        // 学生姓名
    private String studentNumber;      // 学号
    private Integer score;             // 分数
    private String comment;            // 评语
    private Long submissionId;         // 提交ID
    private Date submitTime;           // 提交时间
    private Date scoreTime;            // 评分时间
    private Long teacherId;            // 评分教师ID
    private String teacherName;        // 评分教师姓名
} 