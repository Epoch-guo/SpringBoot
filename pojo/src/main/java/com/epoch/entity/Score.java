package com.epoch.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 评分实体类
 */
@Data
@Schema(description = "评分实体")
public class Score {

    /**
     * 评分ID
     */
    @Schema(description = "评分ID")
    private Long id;

    /**
     * 提交ID
     */
    @Schema(description = "提交ID")
    private Long submissionId;

    /**
     * 评分教师ID
     */
    @Schema(description = "评分教师ID")
    private Long teacherId;

    /**
     * 分数
     */
    @Schema(description = "分数")
    private Integer score;

    /**
     * 评语
     */
    @Schema(description = "评语")
    private String comment;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
} 