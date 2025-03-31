package com.epoch.service;

import com.epoch.dto.QuestionDTO;
import com.epoch.vo.QuestionVO;

import java.util.List;

/**
 * 题目服务接口
 */
public interface QuestionService {
    
    /**
     * 添加题目
     * @param questionDTO 题目DTO
     * @return 题目ID
     */
    Long addQuestion(QuestionDTO questionDTO);
    
    /**
     * 批量添加题目
     * @param questionDTOList 题目DTO列表
     * @return 添加成功的数量
     */
    int batchAddQuestions(List<QuestionDTO> questionDTOList);
    
    /**
     * 更新题目
     * @param questionDTO 题目DTO
     * @return 是否成功
     */
    boolean updateQuestion(QuestionDTO questionDTO);
    
    /**
     * 根据ID查询题目
     * @param id 题目ID
     * @return 题目VO
     */
    QuestionVO getQuestionById(Long id);
    
    /**
     * 查询所有题目
     * @return 题目VO列表
     */
    List<QuestionVO> getAllQuestions();
    
    /**
     * 根据竞赛ID查询题目列表
     * @param contestId 竞赛ID
     * @return 题目VO列表
     */
    List<QuestionVO> getQuestionsByContestId(Long contestId);
    
    /**
     * 删除题目
     * @param id 题目ID
     * @return 是否成功
     */
    boolean deleteQuestion(Long id);
    
    /**
     * 根据竞赛ID删除题目
     * @param contestId 竞赛ID
     * @return 删除数量
     */
    int deleteQuestionsByContestId(Long contestId);
} 