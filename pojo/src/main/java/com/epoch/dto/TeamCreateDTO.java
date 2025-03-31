package com.epoch.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * 团队创建DTO
 */
@Data
@Schema(description = "团队创建DTO")
public class TeamCreateDTO {
    
    @NotBlank(message = "团队名称不能为空")
    @Size(max = 50, message = "团队名称长度不能超过50个字符")
    @Schema(description = "团队名称", required = true)
    private String teamName;
    
    @Size(max = 500, message = "团队描述长度不能超过500个字符")
    @Schema(description = "团队描述")
    private String teamDescription;
    
    @Schema(description = "最大成员数")
    private Integer maxMembers = 5;

    @NotNull(message = "竞赛ID不能为空")
    @Schema(description = "竞赛ID", required = true)
    private Long contestId;
    
    @Schema(description = "成员ID列表")
    private List<Long> memberIds;
    
    @Schema(description = "队长ID")
    private Long leaderId;

    /**
     * 评分DTO
     */
    @Data
    public static class ScoreDTO {

        private Long scoreId;            // 评分ID
        private Long submissionId;        // 提交ID
        private Long teacherId;           // 教师ID
        private Integer manualScore;      // 分数
        private String comments;          // 评语
    }
}