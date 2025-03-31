package com.epoch.mapper;

import com.epoch.entity.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 题目Mapper接口
 */
@Mapper
public interface QuestionMapper {
    
    /**
     * 保存题目
     * @param question 题目对象
     * @return 影响行数
     */
    int insert(Question question);
    
    /**
     * 批量保存题目
     * @param questions 题目列表
     * @return 影响行数
     */
    int batchInsert(List<Question> questions);
    
    /**
     * 更新题目
     * @param question 题目对象
     * @return 影响行数
     */
    int update(Question question);
    
    /**
     * 根据ID查询题目
     * @param id 题目ID
     * @return 题目对象
     */
    Question getById(Long id);
    
    /**
     * 查询所有题目
     * @return 题目列表
     */
    List<Question> listAll();
    
    /**
     * 根据竞赛ID查询题目列表
     * @param contestId 竞赛ID
     * @return 题目列表
     */
    List<Question> listByContestId(Long contestId);
    
    /**
     * 删除题目
     * @param id 题目ID
     * @return 影响行数
     */
    int deleteById(Long id);
    
    /**
     * 根据竞赛ID删除题目
     * @param contestId 竞赛ID
     * @return 影响行数
     */
    int deleteByContestId(Long contestId);
    
    /**
     * 查询所有竞赛ID
     * @return 竞赛ID列表
     */
    List<Long> listAllContestIds();
} 