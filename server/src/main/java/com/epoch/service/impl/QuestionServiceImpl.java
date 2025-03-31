package com.epoch.service.impl;

import com.epoch.dto.QuestionDTO;
import com.epoch.entity.Question;
import com.epoch.mapper.ContestMapper;
import com.epoch.mapper.QuestionMapper;
import com.epoch.service.QuestionService;
import com.epoch.vo.QuestionVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 题目服务实现
 */
@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;
    
    @Autowired
    private ContestMapper contestMapper;

    @Override
    @Transactional
    public Long addQuestion(QuestionDTO questionDTO) {
        Question question = new Question();
        BeanUtils.copyProperties(questionDTO, question);
        question.setId(questionDTO.getQuestionId());
        questionMapper.insert(question);
        return question.getId();
    }

    @Override
    @Transactional
    public int batchAddQuestions(List<QuestionDTO> questionDTOList) {
        if (questionDTOList == null || questionDTOList.isEmpty()) {
            return 0;
        }
        
        List<Question> questions = questionDTOList.stream()
                .map(dto -> {
                    Question question = new Question();
                    BeanUtils.copyProperties(dto, question);
                    question.setId(dto.getQuestionId());
                    return question;
                })
                .collect(Collectors.toList());
        
        return questionMapper.batchInsert(questions);
    }

    @Override
    @Transactional
    public boolean updateQuestion(QuestionDTO questionDTO) {
        if (questionDTO.getQuestionId() == null) {
            return false;
        }
        
        Question question = new Question();
        BeanUtils.copyProperties(questionDTO, question);
        question.setId(questionDTO.getQuestionId());
        return questionMapper.update(question) > 0;
    }

    @Override
    public QuestionVO getQuestionById(Long id) {
        Question question = questionMapper.getById(id);
        if (question == null) {
            return null;
        }
        
        return convertToVO(question);
    }

    @Override
    public List<QuestionVO> getAllQuestions() {
        List<Question> questions = questionMapper.listAll();
        if (questions == null || questions.isEmpty()) {
            return new ArrayList<>();
        }
        
        return questions.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<QuestionVO> getQuestionsByContestId(Long contestId) {
        List<Question> questions = questionMapper.listByContestId(contestId);
        if (questions == null || questions.isEmpty()) {
            return new ArrayList<>();
        }
        
        return questions.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean deleteQuestion(Long id) {
        return questionMapper.deleteById(id) > 0;
    }

    @Override
    @Transactional
    public int deleteQuestionsByContestId(Long contestId) {
        return questionMapper.deleteByContestId(contestId);
    }
    
    /**
     * 将实体转换为VO
     * @param question 题目实体
     * @return 题目VO
     */
    private QuestionVO convertToVO(Question question) {
        QuestionVO vo = new QuestionVO();
        BeanUtils.copyProperties(question, vo);
        vo.setQuestionId(question.getId());
        
        // 获取竞赛标题
        if (question.getContestId() != null) {
            com.epoch.entity.Contest contest = contestMapper.getById(question.getContestId());
            if (contest != null) {
                // 由于QuestionVO没有支持设置竞赛标题或ID的方法，暂时跳过
                // TODO: 如果需要显示竞赛标题，需要在QuestionVO中添加contestTitle字段
            }
        }
        
        return vo;
    }
} 