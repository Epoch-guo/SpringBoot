package com.epoch.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 提交实体类
 */
@Data
@Schema(description = "提交实体")
public class Submission {

    @Schema(description = "提交ID")
    private Long id;

    @Schema(description = "比赛ID")
    private Long contestId;

    @Schema(description = "学生ID")
    private Long studentId;

    @Schema(description = "提交内容")
    private String content;

    @Schema(description = "附件路径")
    private String attachmentPath;

    @Schema(description = "状态（0-待审核，1-已通过，2-已拒绝）")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
} 