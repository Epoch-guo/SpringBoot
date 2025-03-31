package com.epoch.service;

import com.epoch.dto.ScoreDTO;
import com.epoch.vo.ScoreVO;
import com.epoch.vo.ScoreRankingVO;

import java.util.List;
import java.util.Map;

/**
 * 评分服务接口
 */
public interface ScoreService {
    
    /**
     * 添加评分
     * @param scoreDTO 评分信息
     * @param teacherId 教师ID
     * @return 评分ID
     */
    Long addScore(ScoreDTO scoreDTO, Long teacherId);
    
    /**
     * 更新评分
     * @param scoreDTO 评分信息
     * @return 是否成功
     */
    boolean updateScore(ScoreDTO scoreDTO);
    
    /**
     * 根据ID获取评分
     * @param scoreId 评分ID
     * @return 评分信息
     */
    ScoreVO getScoreById(Long scoreId);
    
    /**
     * 根据提交ID获取评分
     * @param submissionId 提交ID
     * @return 评分信息
     */
    ScoreVO getScoreBySubmissionId(Long submissionId);
    
    /**
     * 根据竞赛ID获取评分列表（从视图获取）
     * @param contestId 竞赛ID
     * @return 评分列表
     */
    List<ScoreVO> getScoresByContestIdFromView(Long contestId);
    
    /**
     * 根据竞赛ID和教师ID获取评分列表
     * @param contestId 竞赛ID
     * @param teacherId 教师ID
     * @return 评分列表
     */
    List<ScoreVO> getScoresByContestIdAndTeacherId(Long contestId, Long teacherId);
    
    /**
     * 根据教师ID获取评分列表
     * @param teacherId 教师ID
     * @return 评分列表
     */
    List<ScoreVO> getScoresByTeacherId(Long teacherId);
    
    /**
     * 根据学生ID获取评分列表
     * @param studentId 学生ID
     * @return 评分列表
     */
    List<ScoreVO> getScoresByStudentId(Long studentId);
    
    /**
     * 删除评分
     * @param scoreId 评分ID
     * @return 是否成功
     */
    boolean deleteScore(Long scoreId);
    
    /**
     * 根据提交ID删除评分
     * @param submissionId 提交ID
     * @return 是否成功
     */
    boolean deleteScoreBySubmissionId(Long submissionId);
    
    /**
     * 获取成绩排名
     * @param contestId 竞赛ID
     * @return 成绩排名列表
     */
    List<ScoreRankingVO> getScoreRanking(Long contestId);
    
    /**
     * 获取评分分布统计
     * @param contestId 竞赛ID
     * @return 评分分布统计数据
     */
    Map<String, Integer> getScoreDistribution(Long contestId);
    
    /**
     * 获取教师评分工作量统计
     * @return 教师评分工作量统计数据
     */
    List<Map<String, Object>> getTeacherWorkloadStatistics();
} 