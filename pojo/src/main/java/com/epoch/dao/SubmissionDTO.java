package com.epoch.dao;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 提交DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "题目提交请求参数")
public class SubmissionDTO {
    
    @Schema(description = "题目ID")
    private Long questionId;
    
    @Schema(description = "代码/文本内容")
    private String content;
} 