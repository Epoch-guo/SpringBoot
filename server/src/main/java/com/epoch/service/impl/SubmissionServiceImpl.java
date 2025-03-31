package com.epoch.service.impl;

import com.epoch.constant.MessageConstant;
import com.epoch.dao.SubmissionDTO;
import com.epoch.dto.ScoreDTO;
import com.epoch.entity.Contest;
import com.epoch.entity.Question;
import com.epoch.entity.Score;
import com.epoch.entity.Submission;
import com.epoch.entity.User;
import com.epoch.exception.BaseException;
import com.epoch.mapper.ContestMapper;
import com.epoch.mapper.QuestionMapper;
import com.epoch.mapper.ScoreMapper;
import com.epoch.mapper.SubmissionMapper;
import com.epoch.mapper.UserMapper;
import com.epoch.service.SubmissionService;
import com.epoch.vo.SubmissionVO;
import com.epoch.vo.SubmissionDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 提交记录服务实现类
 */
@Service
@Slf4j
public class SubmissionServiceImpl implements SubmissionService {

    @Autowired
    private SubmissionMapper submissionMapper;
    
    @Autowired
    private ScoreMapper scoreMapper;
    
    @Autowired
    private QuestionMapper questionMapper;
    
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ContestMapper contestMapper;

    /**
     * 提交题目
     * @param submissionDTO 提交信息
     * @param userId 用户ID
     * @param file 提交文件
     * @return 提交记录ID
     */
    @Override
    @Transactional
    public Long submitQuestion(SubmissionDTO submissionDTO, Long userId, MultipartFile file) {
        // 查询题目
        Question question = questionMapper.getById(submissionDTO.getQuestionId());
        if (question == null) {
            throw new BaseException("题目不存在");
        }
        
        // 上传文件
        String filePath = null;
        if (file != null && !file.isEmpty()) {
            try {
                // 生成文件名
                String originalFilename = file.getOriginalFilename();
                String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
                String fileName = UUID.randomUUID().toString() + suffix;
                
                // 保存文件
                String uploadPath = "uploads/submissions/";
                File dir = new File(uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                
                File destFile = new File(dir, fileName);
                file.transferTo(destFile);
                
                filePath = uploadPath + fileName;
            } catch (IOException e) {
                log.error("文件上传失败：", e);
                throw new BaseException(MessageConstant.UPLOAD_FAILED);
            }
        }
        
        // 创建提交记录
        Submission submission = new Submission();
        submission.setStudentId(userId);
        submission.setContestId(question.getContestId());
        submission.setContent(submissionDTO.getContent());
        submission.setAttachmentPath(filePath);
        submission.setStatus(0); // 初始状态为待审核
        
        submissionMapper.insert(submission);
        
        // 如果是选择题，进行自动评分
        if ("choice".equals(question.getType()) && question.getStandardAnswer() != null) {
            int score = 0;
            if (question.getStandardAnswer().equals(submissionDTO.getContent())) {
                score = 100; // 选择题答对得满分
            }
            
            // 创建评分记录
            Score scoreEntity = new Score();
            scoreEntity.setSubmissionId(submission.getId());
            scoreEntity.setTeacherId(0L); // 系统自动评分
            scoreEntity.setScore(score);
            scoreEntity.setComment("系统自动评分");
            
            scoreMapper.insert(scoreEntity);
        }
        
        return submission.getId();
    }

    /**
     * 获取提交详情
     * @param submissionId 提交记录ID
     * @return 提交详情
     */
    @Override
    public SubmissionVO getSubmissionDetail(Long submissionId) {
        // 查询提交记录
        Submission submission = submissionMapper.getById(submissionId);
        if (submission == null) {
            throw new BaseException(MessageConstant.SUBMISSION_NOT_FOUND);
        }
        
        return buildSubmissionVO(submission);
    }

    /**
     * 查询用户提交记录列表
     * @param userId 用户ID
     * @return 提交记录列表
     */
    @Override
    public List<SubmissionVO> listByUserId(Long userId) {
        // 这里需要修改，因为新的SubmissionMapper没有listByUserId方法
        // 我们可以使用listByContestId方法，然后在内存中过滤
        List<Submission> allSubmissions = new ArrayList<>();
        // 获取所有竞赛
        List<Long> contestIds = questionMapper.listAllContestIds();
        for (Long contestId : contestIds) {
            List<Submission> submissions = submissionMapper.listByContestId(contestId);
            allSubmissions.addAll(submissions);
        }
        
        // 过滤出当前用户的提交
        List<Submission> userSubmissions = allSubmissions.stream()
                .filter(s -> s.getStudentId().equals(userId))
                .toList();
        
        return buildSubmissionVOList(userSubmissions);
    }

    /**
     * 查询题目提交记录列表
     * @param questionId 题目ID
     * @return 提交记录列表
     */
    @Override
    public List<SubmissionVO> listByQuestionId(Long questionId) {
        // 这里需要修改，因为新的SubmissionMapper没有listByQuestionId方法
        // 我们可以先获取题目所属的竞赛ID，然后获取该竞赛的所有提交
        Question question = questionMapper.getById(questionId);
        if (question == null) {
            throw new BaseException("题目不存在");
        }
        
        List<Submission> submissions = submissionMapper.listByContestId(question.getContestId());
        
        return buildSubmissionVOList(submissions);
    }

    /**
     * 查询竞赛提交记录列表
     * @param contestId 竞赛ID
     * @return 提交记录列表
     */
    @Override
    public List<SubmissionVO> listByContestId(Long contestId) {
        List<Submission> submissions = submissionMapper.listByContestId(contestId);
        return buildSubmissionVOList(submissions);
    }

    /**
     * 评分
     * @param submissionId 提交记录ID
     * @param scoreDTO 评分信息
     * @param teacherId 教师ID
     * @return 是否成功
     */
    @Override
    @Transactional
    public boolean scoreSubmission(Long submissionId, ScoreDTO scoreDTO, Long teacherId) {
        // 查询提交记录
        Submission submission = submissionMapper.getById(submissionId);
        if (submission == null) {
            throw new BaseException(MessageConstant.SUBMISSION_NOT_FOUND);
        }
        
        // 查询是否已有评分
        Score existScore = scoreMapper.getBySubmissionId(submissionId);
        
        if (existScore != null) {
            // 更新评分
            existScore.setScore(scoreDTO.getManualScore());
            existScore.setComment(scoreDTO.getComments());
            scoreMapper.update(existScore);
        } else {
            // 创建评分
            Score score = new Score();
            score.setSubmissionId(submissionId);
            score.setTeacherId(teacherId);
            score.setScore(scoreDTO.getManualScore());
            score.setComment(scoreDTO.getComments());
            
            scoreMapper.insert(score);
        }
        
        return true;
    }

    /**
     * 删除提交记录
     * @param submissionId 提交记录ID
     * @return 是否成功
     */
    @Override
    @Transactional
    public boolean deleteSubmission(Long submissionId) {
        // 删除评分 - 这里需要自己实现，因为新的ScoreMapper没有deleteBySubmissionId方法
        Score score = scoreMapper.getBySubmissionId(submissionId);
        if (score != null) {
            // 假设ScoreMapper有一个deleteById方法
            // scoreMapper.deleteById(score.getId());
            
            // 如果没有，可以通过SQL直接删除
            // 这里需要添加一个新方法到ScoreMapper
        }
        
        // 删除提交记录 - 这里需要自己实现，因为新的SubmissionMapper没有deleteById方法
        // 假设SubmissionMapper有一个deleteById方法
        // submissionMapper.deleteById(submissionId);
        
        // 如果没有，可以通过SQL直接删除
        // 这里需要添加一个新方法到SubmissionMapper
        
        return true;
    }
    
    /**
     * 构建提交记录VO
     * @param submission 提交记录
     * @return 提交记录VO
     */
    private SubmissionVO buildSubmissionVO(Submission submission) {
        SubmissionVO submissionVO = SubmissionVO.builder()
                .submissionId(submission.getId())
                .userId(submission.getStudentId())
                .questionId(0L) // 这里需要根据contestId查询对应的questionId
                .content(submission.getContent())
                .filePath(submission.getAttachmentPath())
                .autoScore(0) // 这里需要从Score表中获取
                .submissionTime(submission.getCreateTime())
                .build();
        
        // 查询用户信息
        User user = userMapper.getById(submission.getStudentId());
        if (user != null) {
            submissionVO.setUsername(user.getUsername());
        }
        
        // 查询评分信息
        Score score = scoreMapper.getBySubmissionId(submission.getId());
        if (score != null) {
            submissionVO.setManualScore(score.getScore());
            submissionVO.setComments(score.getComment());
            submissionVO.setTeacherId(score.getTeacherId());
            submissionVO.setScoredTime(score.getCreateTime());
            
            // 查询教师信息
            User teacher = userMapper.getById(score.getTeacherId());
            if (teacher != null) {
                submissionVO.setTeacherName(teacher.getUsername());
            }
        }
        
        return submissionVO;
    }
    
    /**
     * 构建提交记录VO列表
     * @param submissions 提交记录列表
     * @return 提交记录VO列表
     */
    private List<SubmissionVO> buildSubmissionVOList(List<Submission> submissions) {
        List<SubmissionVO> submissionVOS = new ArrayList<>();
        
        for (Submission submission : submissions) {
            submissionVOS.add(buildSubmissionVO(submission));
        }
        
        return submissionVOS;
    }

    @Override
    public List<SubmissionDetailVO> getPendingSubmissions(Long contestId) {
        List<Submission> submissions = submissionMapper.listPendingSubmissions(contestId);
        if (submissions == null || submissions.isEmpty()) {
            return new ArrayList<>();
        }
        return submissions.stream()
                .map(this::convertToDetailVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SubmissionDetailVO> getScoredSubmissions(Long contestId) {
        List<Submission> submissions = submissionMapper.listScoredSubmissions(contestId);
        if (submissions == null || submissions.isEmpty()) {
            return new ArrayList<>();
        }
        return submissions.stream()
                .map(this::convertToDetailVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SubmissionDetailVO> getSubmissionsByStudentAndContest(Long studentId, Long contestId) {
        // 获取该学生在该竞赛的所有提交记录
        List<Submission> submissions = submissionMapper.listByStudentIdAndContestId(studentId, contestId);
        if (submissions == null || submissions.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 将记录转换为VO对象
        return submissions.stream()
                .map(this::convertToDetailVO)
                .collect(Collectors.toList());
    }

    /**
     * 将提交实体转换为详情VO
     * @param submission 提交实体
     * @return 提交详情VO
     */
    private SubmissionDetailVO convertToDetailVO(Submission submission) {
        SubmissionDetailVO vo = new SubmissionDetailVO();
        vo.setSubmissionId(submission.getId());
        vo.setStudentId(submission.getStudentId());
        vo.setContestId(submission.getContestId());
        vo.setContent(submission.getContent());
        vo.setAttachmentPath(submission.getAttachmentPath());
        vo.setStatus(submission.getStatus());
        vo.setSubmitTime(submission.getCreateTime());
        
        // 获取竞赛信息
        Contest contest = contestMapper.getById(submission.getContestId());
        if (contest != null) {
            vo.setContestTitle(contest.getTitle());
        }
        
        // 获取学生信息
        User student = userMapper.getById(submission.getStudentId());
        if (student != null) {
            vo.setStudentName(student.getUsername());
        }
        
        // 获取评分信息
        Score score = scoreMapper.getBySubmissionId(submission.getId());
        if (score != null) {
            vo.setScoreId(score.getId());
            vo.setScore(score.getScore());
            vo.setComment(score.getComment());
            vo.setTeacherId(score.getTeacherId());
            vo.setScoreTime(java.sql.Timestamp.valueOf(score.getCreateTime()));
            
            // 获取教师信息
            User teacher = userMapper.getById(score.getTeacherId());
            if (teacher != null) {
                vo.setTeacherName(teacher.getUsername());
            }
        }
        
        return vo;
    }
} 