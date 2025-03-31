package com.epoch.mapper;

import com.epoch.entity.Submission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 提交Mapper接口
 */
@Mapper
public interface SubmissionMapper {
    
    /**
     * 插入提交记录
     * @param submission 提交对象
     * @return 影响的行数
     */
    int insert(Submission submission);
    
    /**
     * 更新提交记录
     * @param submission 提交对象
     * @return 影响的行数
     */
    int update(Submission submission);
    
    /**
     * 根据ID获取提交记录
     * @param id 提交ID
     * @return 提交记录
     */
    Submission getById(Long id);
    
    /**
     * 根据比赛ID获取所有提交记录
     * @param contestId 比赛ID
     * @return 提交记录列表
     */
    List<Submission> listByContestId(Long contestId);
    
    /**
     * 根据学生ID获取所有提交记录
     * @param studentId 学生ID
     * @return 提交记录列表
     */
    List<Submission> listByStudentId(Long studentId);
    
    /**
     * 根据学生ID和比赛ID获取提交记录
     * @param studentId 学生ID
     * @param contestId 比赛ID
     * @return 提交记录
     */
    Submission getByStudentIdAndContestId(@Param("studentId") Long studentId, @Param("contestId") Long contestId);
    
    /**
     * 根据学生ID和比赛ID获取提交记录列表
     * @param studentId 学生ID
     * @param contestId 比赛ID
     * @return 提交记录列表
     */
    List<Submission> listByStudentIdAndContestId(@Param("studentId") Long studentId, @Param("contestId") Long contestId);
    
    /**
     * 根据ID删除提交记录
     * @param id 提交ID
     * @return 影响的行数
     */
    int deleteById(Long id);
    
    /**
     * 获取竞赛的待评分提交列表
     * @param contestId 竞赛ID
     * @return 提交列表
     */
    List<Submission> listPendingSubmissions(Long contestId);

    /**
     * 获取竞赛的已评分提交列表
     * @param contestId 竞赛ID
     * @return 提交列表
     */
    List<Submission> listScoredSubmissions(Long contestId);

    List<Submission> listByContestIdAndStatus(@Param("contestId") Long contestId, @Param("status") Integer status);
    
    Integer countByContestId(@Param("contestId") Long contestId);
    
    Integer countByContestIdAndStatus(@Param("contestId") Long contestId, @Param("status") Integer status);
} 