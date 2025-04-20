package com.epoch.dto;

import lombok.Data;

/**
 * 题目DTO
 */
@Data
public class QuestionDTO {
    
    private Long id;               // 题目ID
    private Long contestId;        // 竞赛ID
    private String type;           // 题型
    private String content;        // 题目内容
    private String standardAnswer; // 选择题标准答案
    private String scoringCriteria; // 主观题评分维度
    
    // 兼容方法
    public Long getQuestionId() {
        return id;
    }
    
    public void setQuestionId(Long id) {
        this.id = id;
    }
} 