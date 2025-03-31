package com.epoch.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 成绩排名数据展示对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "成绩排名数据展示对象")
public class ScoreRankingVO {
    
    @Schema(description = "排名")
    private Integer rank;
    
    @Schema(description = "学生ID")
    private Long studentId;
    
    @Schema(description = "学生姓名")
    private String studentName;
    
    @Schema(description = "学号")
    private String studentNumber;
    
    @Schema(description = "分数")
    private Integer score;
    
    @Schema(description = "评语")
    private String comment;
    
    @Schema(description = "提交ID")
    private Long submissionId;
    
    @Schema(description = "提交时间")
    private LocalDateTime submitTime;
    
    @Schema(description = "评分时间")
    private LocalDateTime scoreTime;
    
    @Schema(description = "评分教师ID")
    private Long teacherId;
    
    @Schema(description = "评分教师姓名")
    private String teacherName;
} 