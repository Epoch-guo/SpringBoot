package com.epoch.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 提交VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "提交记录信息")
public class SubmissionVO {
    
    @Schema(description = "提交ID")
    private Long submissionId;
    
    @Schema(description = "用户ID")
    private Long userId;
    
    @Schema(description = "用户名")
    private String username;
    
    @Schema(description = "题目ID")
    private Long questionId;
    
    @Schema(description = "题目内容")
    private String questionContent;
    
    @Schema(description = "题目类型")
    private String questionType;
    
    @Schema(description = "代码/文本内容")
    private String content;
    
    @Schema(description = "文件路径")
    private String filePath;
    
    @Schema(description = "自动评分")
    private Integer autoScore;
    
    @Schema(description = "手动评分")
    private Integer manualScore;
    
    @Schema(description = "评语")
    private String comments;
    
    @Schema(description = "评分教师ID")
    private Long teacherId;
    
    @Schema(description = "评分教师名称")
    private String teacherName;
    
    @Schema(description = "提交时间")
    private LocalDateTime submissionTime;
    
    @Schema(description = "评分时间")
    private LocalDateTime scoredTime;
} 