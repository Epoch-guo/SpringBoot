package com.epoch.mapper;

import com.epoch.entity.Score;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 评分Mapper接口
 */
@Mapper
public interface ScoreMapper {
    
    /**
     * 插入评分记录
     * @param score 评分实体
     * @return 影响行数
     */
    int insert(Score score);
    
    /**
     * 更新评分记录
     * @param score 评分实体
     * @return 影响行数
     */
    int update(Score score);
    
    /**
     * 根据ID查询评分
     * @param scoreId 评分ID
     * @return 评分实体
     */
    Score getById(Long scoreId);
    
    /**
     * 根据提交ID查询评分
     * @param submissionId 提交ID
     * @return 评分实体
     */
    Score getBySubmissionId(Long submissionId);
    
    /**
     * 根据竞赛ID查询评分列表（从视图）
     * @param contestId 竞赛ID
     * @return 评分列表
     */
    List<Score> listByContestIdFromView(Long contestId);
    
    /**
     * 根据教师ID查询评分列表
     * @param teacherId 教师ID
     * @return 评分列表
     */
    List<Score> listByTeacherId(Long teacherId);
    
    /**
     * 根据学生ID查询评分列表
     * @param studentId 学生ID
     * @return 评分列表
     */
    List<Score> listByStudentId(Long studentId);
    
    /**
     * 根据ID删除评分
     * @param scoreId 评分ID
     * @return 影响行数
     */
    int deleteById(Long scoreId);
    
    /**
     * 根据提交ID删除评分
     * @param submissionId 提交ID
     * @return 影响行数
     */
    int deleteBySubmissionId(Long submissionId);
    
    /**
     * 根据比赛ID获取所有评分记录
     * @param contestId 比赛ID
     * @return 评分记录列表
     */
    List<Score> listByContestId(Long contestId);
    
    /**
     * 根据比赛ID和班级ID获取所有评分记录
     * @param contestId 比赛ID
     * @param classId 班级ID
     * @return 评分记录列表
     */
    List<Score> listByContestIdAndClassId(@Param("contestId") Long contestId, @Param("classId") Long classId);
    
    /**
     * 根据提交ID列表获取评分记录
     * @param submissionIds 提交ID列表
     * @return 评分记录列表
     */
    List<Score> listBySubmissionIds(@Param("submissionIds") List<Long> submissionIds);
    
    /**
     * 根据竞赛ID和教师ID查询评分记录
     * @param contestId 竞赛ID
     * @param teacherId 教师ID
     * @return 评分列表
     */
    List<Score> listByContestIdAndTeacherId(@Param("contestId") Long contestId, @Param("teacherId") Long teacherId);

    Integer countScoresByRange(@Param("contestId") Long contestId, @Param("minScore") Integer minScore, @Param("maxScore") Integer maxScore);

    Integer countByTeacherId(@Param("teacherId") Long teacherId);

    Double getAverageScoreByTeacherId(@Param("teacherId") Long teacherId);
    
    Double getAverageScoreByContestId(@Param("contestId") Long contestId);
    
    Integer getHighestScoreByContestId(@Param("contestId") Long contestId);
    
    Integer getLowestScoreByContestId(@Param("contestId") Long contestId);
} 