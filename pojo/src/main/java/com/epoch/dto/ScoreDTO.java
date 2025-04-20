package com.epoch.dto;

import lombok.Data;

/**
 * 评分DTO
 */
@Data
public class ScoreDTO {
    
    private Long id;                // 评分ID
    private Long submissionId;      // 提交ID
    private Long teacherId;         // 教师ID
    private Integer score;          // 分数
    private String comment;         // 评语
    
    // 为了兼容性添加的方法
    public Long getScoreId() {
        return id;
    }
    
    public void setScoreId(Long id) {
        this.id = id;
    }
    
    public Integer getManualScore() {
        return score;
    }
    
    public String getComments() {
        return comment;
    }
} 