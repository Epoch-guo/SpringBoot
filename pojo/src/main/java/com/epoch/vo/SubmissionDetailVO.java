package com.epoch.vo;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 提交详情VO
 */
@Data
public class SubmissionDetailVO {
    
    private Long id;                  // 提交ID
    private Long contestId;           // 竞赛ID
    private String contestTitle;      // 竞赛标题
    private Long studentId;           // 学生ID
    private String studentName;       // 学生姓名
    private String studentNumber;     // 学号
    private String content;           // 提交内容
    private String attachmentPath;    // 附件路径
    private Integer status;           // 状态
    private Date createTime;          // 创建时间
    private Date updateTime;          // 更新时间
    
    // 评分相关信息
    private Long scoreId;             // 评分ID
    private Long teacherId;           // 教师ID
    private String teacherName;       // 教师姓名
    private Integer score;            // 分数
    private String comment;           // 评语
    private Date scoreTime;           // 评分时间
    
    // 兼容方法
    public void setSubmissionId(Long id) {
        this.id = id;
    }
    
    public void setSubmitTime(LocalDateTime createTime) {
        if (createTime != null) {
            this.createTime = java.sql.Timestamp.valueOf(createTime);
        }
    }
} 