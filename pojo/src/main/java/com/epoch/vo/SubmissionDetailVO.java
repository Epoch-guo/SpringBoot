package com.epoch.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 提交详情VO
 */
@Data
@Schema(description = "提交详情展示对象")
public class SubmissionDetailVO {
    
    @Schema(description = "提交ID")
    private Long submissionId;
    
    @Schema(description = "竞赛ID")
    private Long contestId;
    
    @Schema(description = "竞赛标题")
    private String contestTitle;
    
    @Schema(description = "学生ID")
    private Long studentId;
    
    @Schema(description = "学生姓名")
    private String studentName;
    
    @Schema(description = "提交内容")
    private String content;
    
    @Schema(description = "附件路径")
    private String attachmentPath;
    
    @Schema(description = "状态")
    private Integer status;
    
    @Schema(description = "提交时间")
    private LocalDateTime submitTime;
    
    @Schema(description = "评分ID")
    private Long scoreId;
    
    @Schema(description = "分数")
    private Integer score;
    
    @Schema(description = "评语")
    private String comment;
    
    @Schema(description = "评分教师ID")
    private Long teacherId;
    
    @Schema(description = "评分教师姓名")
    private String teacherName;
    
    @Schema(description = "评分时间")
    private LocalDateTime scoreTime;
} 