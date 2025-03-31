package com.epoch.service;

import com.epoch.dto.ScoreDTO;
import com.epoch.dao.SubmissionDTO;
import com.epoch.vo.SubmissionVO;
import com.epoch.vo.SubmissionDetailVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 提交服务接口
 */
public interface SubmissionService {
    
    /**
     * 提交题目
     * @param submissionDTO 提交信息
     * @param userId 用户ID
     * @param file 提交文件
     * @return 提交记录ID
     */
    Long submitQuestion(SubmissionDTO submissionDTO, Long userId, MultipartFile file);
    
    /**
     * 获取提交详情
     * @param submissionId 提交记录ID
     * @return 提交详情
     */
    SubmissionVO getSubmissionDetail(Long submissionId);
    
    /**
     * 查询用户提交记录列表
     * @param userId 用户ID
     * @return 提交记录列表
     */
    List<SubmissionVO> listByUserId(Long userId);
    
    /**
     * 查询题目提交记录列表
     * @param questionId 题目ID
     * @return 提交记录列表
     */
    List<SubmissionVO> listByQuestionId(Long questionId);
    
    /**
     * 查询竞赛提交记录列表
     * @param contestId 竞赛ID
     * @return 提交记录列表
     */
    List<SubmissionVO> listByContestId(Long contestId);
    
    /**
     * 评分
     * @param submissionId 提交记录ID
     * @param scoreDTO 评分信息
     * @param teacherId 教师ID
     * @return 是否成功
     */
    boolean scoreSubmission(Long submissionId, ScoreDTO scoreDTO, Long teacherId);
    
    /**
     * 删除提交记录
     * @param submissionId 提交记录ID
     * @return 是否成功
     */
    boolean deleteSubmission(Long submissionId);
    
    /**
     * 获取竞赛的待评分提交列表
     * @param contestId 竞赛ID
     * @return 提交列表
     */
    List<SubmissionDetailVO> getPendingSubmissions(Long contestId);
    
    /**
     * 获取竞赛的已评分提交列表
     * @param contestId 竞赛ID
     * @return 提交列表
     */
    List<SubmissionDetailVO> getScoredSubmissions(Long contestId);
    
    /**
     * 获取学生在特定竞赛的所有提交
     * @param studentId 学生ID
     * @param contestId 竞赛ID
     * @return 提交列表
     */
    List<SubmissionDetailVO> getSubmissionsByStudentAndContest(Long studentId, Long contestId);
} 