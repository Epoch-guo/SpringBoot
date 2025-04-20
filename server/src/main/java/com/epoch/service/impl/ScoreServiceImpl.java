package com.epoch.service.impl;

import com.epoch.constant.MessageConstant;
import com.epoch.dto.ScoreDTO;
import com.epoch.entity.Score;
import com.epoch.entity.Submission;
import com.epoch.entity.User;
import com.epoch.exception.BaseException;
import com.epoch.mapper.ScoreMapper;
import com.epoch.mapper.SubmissionMapper;
import com.epoch.mapper.UserMapper;
import com.epoch.service.ScoreService;
import com.epoch.vo.ScoreRankingVO;
import com.epoch.vo.ScoreVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 评分服务实现类
 */
@Service
@Slf4j
public class ScoreServiceImpl implements ScoreService {

    @Autowired
    private ScoreMapper scoreMapper;
    
    @Autowired
    private SubmissionMapper submissionMapper;
    
    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public Long addScore(ScoreDTO scoreDTO, Long teacherId) {
        // 检查提交是否存在
        Submission submission = submissionMapper.getById(scoreDTO.getSubmissionId());
        if (submission == null) {
            throw new BaseException(MessageConstant.SUBMISSION_NOT_FOUND);
        }
        
        // 检查是否已有评分
        Score existScore = scoreMapper.getBySubmissionId(scoreDTO.getSubmissionId());
        if (existScore != null) {
            throw new BaseException(MessageConstant.SCORE_ALREADY_EXISTS);
        }
        
        // 创建评分记录
        Score score = new Score();
        score.setSubmissionId(scoreDTO.getSubmissionId());
        score.setTeacherId(teacherId);
        score.setScore(scoreDTO.getManualScore());
        score.setComment(scoreDTO.getComments());
        
        scoreMapper.insert(score);
        return score.getId();
    }

    @Override
    @Transactional
    public boolean updateScore(ScoreDTO scoreDTO) {
        Score score = scoreMapper.getById(scoreDTO.getScoreId());
        if (score == null) {
            throw new BaseException(MessageConstant.SCORE_NOT_FOUND);
        }
        
        score.setScore(scoreDTO.getManualScore());
        score.setComment(scoreDTO.getComments());
        
        return scoreMapper.update(score) > 0;
    }

    @Override
    public ScoreVO getScoreById(Long scoreId) {
        Score score = scoreMapper.getById(scoreId);
        return score != null ? convertToVO(score) : null;
    }

    @Override
    public ScoreVO getScoreBySubmissionId(Long submissionId) {
        Score score = scoreMapper.getBySubmissionId(submissionId);
        return score != null ? convertToVO(score) : null;
    }

    @Override
    public List<ScoreVO> getScoresByContestIdFromView(Long contestId) {
        List<Score> scores = scoreMapper.listByContestIdFromView(contestId);
        return scores.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public List<ScoreVO> getScoresByContestIdAndTeacherId(Long contestId, Long teacherId) {
        List<Score> scores = scoreMapper.listByContestIdAndTeacherId(contestId, teacherId);
        return scores.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public List<ScoreVO> getScoresByTeacherId(Long teacherId) {
        List<Score> scores = scoreMapper.listByTeacherId(teacherId);
        return scores.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public List<ScoreVO> getScoresByStudentId(Long studentId) {
        List<Score> scores = scoreMapper.listByStudentId(studentId);
        return scores.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean deleteScore(Long scoreId) {
        return scoreMapper.deleteById(scoreId) > 0;
    }

    @Override
    @Transactional
    public boolean deleteScoreBySubmissionId(Long submissionId) {
        return scoreMapper.deleteBySubmissionId(submissionId) > 0;
    }

    @Override
    public List<ScoreRankingVO> getScoreRanking(Long contestId) {
        // 获取竞赛所有已评分的提交
        List<Submission> submissions = submissionMapper.listByContestIdAndStatus(contestId, 1); // 状态1表示已评分
        if (submissions.isEmpty()) {
            return Collections.emptyList();
        }
        
        // 获取提交ID列表
        List<Long> submissionIds = submissions.stream()
                .map(Submission::getId)
                .collect(Collectors.toList());
        
        // 获取对应的评分记录
        List<Score> scores = scoreMapper.listBySubmissionIds(submissionIds);
        if (scores.isEmpty()) {
            return Collections.emptyList();
        }
        
        // 构建提交ID到评分的映射
        Map<Long, Score> scoreMap = scores.stream()
                .collect(Collectors.toMap(Score::getSubmissionId, s -> s));
        
        // 组装排名数据
        List<ScoreRankingVO> ranking = new ArrayList<>();
        for (Submission submission : submissions) {
            Score score = scoreMap.get(submission.getId());
            if (score == null) {
                continue;
            }
            
            User student = userMapper.getById(submission.getStudentId());
            User teacher = userMapper.getById(score.getTeacherId());
            
            ScoreRankingVO vo = new ScoreRankingVO();
            vo.setStudentId(submission.getStudentId());
            vo.setStudentName(student != null ? student.getUsername() : null);
            vo.setStudentNumber(student != null ? student.getUsername() : null); // 假设学号就是用户名
            vo.setScore(score.getScore());
            vo.setComment(score.getComment());
            vo.setSubmissionId(submission.getId());
            vo.setSubmitTime(submission.getCreateTime());
            vo.setScoreTime(score.getCreateTime());
            vo.setTeacherId(score.getTeacherId());
            vo.setTeacherName(teacher != null ? teacher.getUsername() : null);
            
            ranking.add(vo);
        }
        
        // 按分数排序
        ranking.sort((a, b) -> b.getScore().compareTo(a.getScore()));
        
        // 添加排名
        for (int i = 0; i < ranking.size(); i++) {
            ranking.get(i).setRank(i + 1);
        }
        
        return ranking;
    }

    @Override
    public Map<String, Integer> getScoreDistribution(Long contestId) {
        Map<String, Integer> distribution = new HashMap<>();
        distribution.put("90-100", scoreMapper.countScoresByRange(contestId, 90, 100));
        distribution.put("80-89", scoreMapper.countScoresByRange(contestId, 80, 89));
        distribution.put("70-79", scoreMapper.countScoresByRange(contestId, 70, 79));
        distribution.put("60-69", scoreMapper.countScoresByRange(contestId, 60, 69));
        distribution.put("0-59", scoreMapper.countScoresByRange(contestId, 0, 59));
        return distribution;
    }

    @Override
    public List<Map<String, Object>> getTeacherWorkloadStatistics() {
        // 获取所有教师
        List<User> teachers = userMapper.listByRole("teacher");
        if (teachers.isEmpty()) {
            return Collections.emptyList();
        }
        
        List<Map<String, Object>> result = new ArrayList<>();
        for (User teacher : teachers) {
            Map<String, Object> item = new HashMap<>();
            item.put("teacherId", teacher.getId());
            item.put("teacherName", teacher.getUsername());
            
            // 查询教师评分的提交数量
            Integer scoredCount = scoreMapper.countByTeacherId(teacher.getId());
            item.put("scoredCount", scoredCount);
            
            // 查询教师评分的平均分
            if (scoredCount > 0) {
                Double avgScore = scoreMapper.getAverageScoreByTeacherId(teacher.getId());
                item.put("averageScore", avgScore);
            } else {
                item.put("averageScore", 0.0);
            }
            
            result.add(item);
        }
        
        // 按评分数量排序
        result.sort((a, b) -> ((Integer) b.get("scoredCount")).compareTo((Integer) a.get("scoredCount")));
        
        return result;
    }

    /**
     * 将评分实体转换为VO
     */
    private ScoreVO convertToVO(Score score) {
        if (score == null) {
            return null;
        }
        
        ScoreVO vo = new ScoreVO();
        BeanUtils.copyProperties(score, vo);
        vo.setScoreId(score.getId());
        
        // 获取提交信息
        Submission submission = submissionMapper.getById(score.getSubmissionId());
        if (submission != null) {
            vo.setStudentName(userMapper.getById(submission.getStudentId()).getUsername());
        }
        
        // 获取教师信息
        vo.setTeacherName(userMapper.getById(score.getTeacherId()).getUsername());
        
        return vo;
    }
}