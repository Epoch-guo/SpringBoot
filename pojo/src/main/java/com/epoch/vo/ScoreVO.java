package com.epoch.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 评分视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "评分展示对象")
public class ScoreVO {
    
    @Schema(description = "评分ID")
    private Long scoreId;
    
    @Schema(description = "提交ID")
    private Long submissionId;
    
    @Schema(description = "竞赛标题")
    private String contestTitle;
    
    @Schema(description = "题目内容")
    private String questionContent;
    
    @Schema(description = "学生姓名")
    private String studentName;
    
    @Schema(description = "评分教师姓名")
    private String teacherName;
    
    @Schema(description = "分数")
    private Integer score;
    
    @Schema(description = "评语")
    private String comment;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
} 