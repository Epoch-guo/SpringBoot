package com.epoch.service.impl;

import com.epoch.constant.MessageConstant;
import com.epoch.dao.ContestDTO;
import com.epoch.dao.QuestionDTO;
import com.epoch.dao.RegistrationDTO;
import com.epoch.entity.Contest;
import com.epoch.entity.Question;
import com.epoch.entity.Registration;
import com.epoch.entity.TeamMember;
import com.epoch.entity.User;
import com.epoch.exception.BaseException;
import com.epoch.mapper.ContestMapper;
import com.epoch.mapper.QuestionMapper;
import com.epoch.mapper.RegistrationMapper;
import com.epoch.mapper.ScoreMapper;
import com.epoch.mapper.SubmissionMapper;
import com.epoch.mapper.TeamMemberMapper;
import com.epoch.mapper.UserMapper;
import com.epoch.result.PageResult;
import com.epoch.service.ContestService;
import com.epoch.vo.ContestListVO;
import com.epoch.vo.ContestRegistrationsVO;
import com.epoch.vo.ContestStatisticsVO;
import com.epoch.vo.ContestVO;
import com.epoch.vo.QuestionVO;
import com.epoch.vo.RegistrationVO;
import com.epoch.vo.TeamMemberVO;
import com.epoch.vo.TeamVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 竞赛服务实现类
 */
@Service
@Slf4j
public class ContestServiceImpl implements ContestService {

    @Autowired
    private ContestMapper contestMapper;
    
    @Autowired
    private QuestionMapper questionMapper;
    
    @Autowired
    private RegistrationMapper registrationMapper;
    
    @Autowired
    private TeamMemberMapper teamMemberMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private SubmissionMapper submissionMapper;
    
    @Autowired
    private ScoreMapper scoreMapper;
    
    private final Gson gson = new Gson();

    /**
     * 创建竞赛
     * @param contestDTO 竞赛信息
     * @param creatorId 创建者ID
     * @return 竞赛ID
     */
    @Override
    @Transactional
    public Long createContest(ContestDTO contestDTO, Long creatorId) {
        // 创建竞赛对象
        Contest contest = Contest.builder()
                .title(contestDTO.getTitle())
                .description(contestDTO.getDescription())
                .startTime(contestDTO.getStartTime())
                .endTime(contestDTO.getEndTime())
                .rules(contestDTO.getRules())
                .awards(contestDTO.getAwards())
                .creatorId(creatorId)
                .status("pending")
                .build();
        
        // 保存竞赛
        contestMapper.insert(contest);
        
        // 保存题目
        List<Question> questions = new ArrayList<>();
        for (QuestionDTO questionDTO : contestDTO.getQuestions()) {
            Question question = Question.builder()
                    .contestId(contest.getId())
                    .type(questionDTO.getType())
                    .content(questionDTO.getContent())
                    .build();
            
            // 根据题型设置不同的属性
            if ("choice".equals(questionDTO.getType())) {
                question.setStandardAnswer(questionDTO.getStandardAnswer());
            } else {
                // 将评分维度转换为JSON字符串
                question.setScoringCriteria(gson.toJson(questionDTO.getScoringCriteria()));
            }
            
            questions.add(question);
        }
        
        if (!questions.isEmpty()) {
            questionMapper.batchInsert(questions);
        }
        
        return contest.getId();
    }

    /**
     * 更新竞赛
     * @param id 竞赛ID
     * @param contestDTO 竞赛信息
     * @return 是否成功
     */
    @Override
    @Transactional
    public boolean updateContest(Long id, ContestDTO contestDTO) {
        // 查询竞赛
        Contest contest = contestMapper.getById(id);
        if (contest == null) {
            throw new BaseException(MessageConstant.CONTEST_NOT_FOUND);
        }
        
        // 只有pending状态的竞赛才能更新
//        if (!"pending".equals(contest.getStatus())) {
//            throw new BaseException("只有未开始的竞赛才能更新");
//        }
        
        // 更新竞赛信息
        contest.setTitle(contestDTO.getTitle());
        contest.setDescription(contestDTO.getDescription());
        contest.setStartTime(contestDTO.getStartTime());
        contest.setEndTime(contestDTO.getEndTime());
        contest.setRules(contestDTO.getRules());
        contest.setStatus(contestDTO.getStatus());
        contest.setAwards(contestDTO.getAwards());
        
        contestMapper.update(contest);
        
        // 删除原有题目
        questionMapper.deleteByContestId(id);
        
        // 保存新题目
        List<Question> questions = new ArrayList<>();
        for (QuestionDTO questionDTO : contestDTO.getQuestions()) {
            Question question = Question.builder()
                    .contestId(id)
                    .type(questionDTO.getType())
                    .content(questionDTO.getContent())
                    .build();
            
            // 根据题型设置不同的属性
            if ("choice".equals(questionDTO.getType())) {
                question.setStandardAnswer(questionDTO.getStandardAnswer());
            } else {
                // 将评分维度转换为JSON字符串
                question.setScoringCriteria(gson.toJson(questionDTO.getScoringCriteria()));
            }
            
            questions.add(question);
        }
        
        if (!questions.isEmpty()) {
            questionMapper.batchInsert(questions);
        }
        
        return true;
    }

    /**
     * 获取竞赛详情
     * @param id 竞赛ID
     * @return 竞赛详情
     */
    @Override
    public ContestVO getContestDetail(Long id) {
        // 查询竞赛
        Contest contest = contestMapper.getById(id);
        if (contest == null) {
            throw new BaseException(MessageConstant.CONTEST_NOT_FOUND);
        }
        
        // 查询创建者信息
        User creator = userMapper.getById(contest.getCreatorId());
        
        // 查询报名人数
        int registrationCount = registrationMapper.countByContestId(id);
        
        // 构建竞赛VO
        ContestVO contestVO = ContestVO.builder()
                .contestId(contest.getId())
                .title(contest.getTitle())
                .description(contest.getDescription())
                .startTime(contest.getStartTime())
                .endTime(contest.getEndTime())
                .rules(contest.getRules())
                .awards(contest.getAwards())
                .creatorId(contest.getCreatorId())
                .creatorName(creator != null ? creator.getUsername() : "")
                .status(contest.getStatus())
                .registrationCount(registrationCount)
                .createdTime(contest.getCreatedTime())
                .build();
        
        // 查询竞赛题目
        List<Question> questions = questionMapper.listByContestId(id);
        if (questions != null && !questions.isEmpty()) {
            List<QuestionVO> questionVOs = new ArrayList<>();
            for (Question question : questions) {
                QuestionVO questionVO = new QuestionVO();
                questionVO.setQuestionId(question.getId());
                questionVO.setType(question.getType());
                questionVO.setContent(question.getContent());
                questionVOs.add(questionVO);
            }
            contestVO.setQuestions(questionVOs);
        }
        
        // 查询竞赛报名信息
        List<Registration> registrations = registrationMapper.listByContestId(id);
        if (registrations != null && !registrations.isEmpty()) {
            ContestRegistrationsVO registrationsVO = new ContestRegistrationsVO();
            registrationsVO.setTotal(registrations.size());
            
            List<TeamVO> teams = new ArrayList<>();
            List<RegistrationVO> individuals = new ArrayList<>();
            
            for (Registration registration : registrations) {
                if (registration.getIsTeam() != null && registration.getIsTeam()) {
                    // 团队报名
                    TeamVO teamVO = new TeamVO();
                    teamVO.setTeamId(registration.getId());
                    teamVO.setTeamName(registration.getTeamName());
                    teamVO.setLeaderId(registration.getUserId());
                    
                    // 获取队长名称
                    User leader = userMapper.getById(registration.getUserId());
                    teamVO.setLeaderName(leader != null ? leader.getUsername() : null);
                    
                    teamVO.setContestId(registration.getContestId());
                    teamVO.setContestName(contest.getTitle());
                    
                    // 获取成员数量
                    List<TeamMember> members = teamMemberMapper.listByRegistrationId(registration.getId());
                    teamVO.setMemberCount(members.size() + 1); // 加上队长
                    
                    teamVO.setStatus(registration.getStatus());
                    teamVO.setCreatedTime(registration.getCreatedTime());
                    
                    teams.add(teamVO);
                } else {
                    // 个人报名
                    // 获取用户名称
                    User user = userMapper.getById(registration.getUserId());
                    
                    RegistrationVO registrationVO = RegistrationVO.builder()
                        .registrationId(registration.getId())
                        .userId(registration.getUserId())
                        .username(user != null ? user.getUsername() : null)
                        .status(registration.getStatus())
                        .contestId(registration.getContestId())
                        .build();
                    
                    individuals.add(registrationVO);
                }
            }
            
            registrationsVO.setTeams(teams);
            registrationsVO.setIndividuals(individuals);
            
            contestVO.setRegistrations(registrationsVO);
        }
        
        return contestVO;
    }

    /**
     * 分页查询竞赛列表
     * @param page 页码
     * @param pageSize 每页数量
     * @param status 状态
     * @return 分页结果
     */
    @Override
    public PageResult pageQuery(int page, int pageSize, String status) {
        // 根据角色进行权限控制
        List<Contest> contestList;
        long total;
        
        // TODO: 此处需要实现基于当前用户角色的判断
        // 由于SecurityUtils存在编译问题，暂时返回所有竞赛
        PageHelper.startPage(page, pageSize);
        Page<Contest> contestPage = (Page<Contest>) contestMapper.list(status);
        contestList = contestPage.getResult();
        total = contestPage.getTotal();
        
        List<ContestListVO> contestListVOS = new ArrayList<>();
        for (Contest contest : contestList) {
            // 查询报名人数
            int registrationCount = registrationMapper.countByContestId(contest.getId());
            
            ContestListVO contestListVO = ContestListVO.builder()
                    .contestId(contest.getId())
                    .title(contest.getTitle())
                    .startTime(contest.getStartTime())
                    .endTime(contest.getEndTime())
                    .status(contest.getStatus())
                    .registrationCount(registrationCount)
                    .build();
            
            contestListVOS.add(contestListVO);
        }
        
        return new PageResult(total, contestListVOS);
    }

    /**
     * 查询教师创建的竞赛列表
     * @param creatorId 创建者ID
     * @return 竞赛列表
     */
    @Override
    public List<ContestListVO> listByCreator(Long creatorId) {
        // 根据角色进行权限控制
        List<Contest> contests;
        
        // TODO: 此处需要实现基于当前用户角色的判断
        // 由于SecurityUtils存在编译问题，暂时返回指定教师创建的所有竞赛
        contests = contestMapper.listByCreatorId(creatorId);
        
        List<ContestListVO> contestListVOS = new ArrayList<>();
        for (Contest contest : contests) {
            // 查询报名人数
            int registrationCount = registrationMapper.countByContestId(contest.getId());
            
            ContestListVO contestListVO = ContestListVO.builder()
                    .contestId(contest.getId())
                    .title(contest.getTitle())
                    .startTime(contest.getStartTime())
                    .endTime(contest.getEndTime())
                    .status(contest.getStatus())
                    .registrationCount(registrationCount)
                    .build();
            
            contestListVOS.add(contestListVO);
        }
        
        return contestListVOS;
    }

    /**
     * 更新竞赛状态
     * @param id 竞赛ID
     * @param status 状态
     * @return 是否成功
     */
    @Override
    public boolean updateStatus(Long id, String status) {
        // 查询竞赛
        Contest contest = contestMapper.getById(id);
        if (contest == null) {
            throw new BaseException(MessageConstant.CONTEST_NOT_FOUND);
        }
        
        // 更新状态
        contestMapper.updateStatus(id, status);
        
        return true;
    }

    /**
     * 删除竞赛
     * @param id 竞赛ID
     * @return 是否成功
     */
    @Override
    @Transactional
    public boolean deleteContest(Long id) {
        // 查询竞赛
        Contest contest = contestMapper.getById(id);
        if (contest == null) {
            throw new BaseException(MessageConstant.CONTEST_NOT_FOUND);
        }
        
        // 只有pending状态的竞赛才能删除
        if (!"pending".equals(contest.getStatus())) {
            throw new BaseException("只有未开始的竞赛才能删除");
        }
        
        // 删除题目
        questionMapper.deleteByContestId(id);
        
        // 查询报名记录
        List<Registration> registrations = registrationMapper.listByContestId(id);
        for (Registration registration : registrations) {
            // 删除团队成员
            teamMemberMapper.deleteByRegistrationId(registration.getId());
            
            // 删除报名记录
            registrationMapper.deleteById(registration.getId());
        }
        
        // 删除竞赛
        contestMapper.deleteById(id);
        
        return true;
    }

    /**
     * 竞赛报名
     * @param contestId 竞赛ID
     * @param registrationDTO 报名信息
     * @param userId 用户ID
     * @param attachment 附件
     * @return 报名ID
     */
    @Override
    @Transactional
    public Long register(Long contestId, RegistrationDTO registrationDTO, Long userId, MultipartFile attachment) {
        // 查询竞赛
        Contest contest = contestMapper.getById(contestId);
        if (contest == null) {
            throw new BaseException(MessageConstant.CONTEST_NOT_FOUND);
        }
        
        // 检查竞赛状态
        if (!"pending".equals(contest.getStatus()) && !"ongoing".equals(contest.getStatus())) {
            throw new BaseException("竞赛已结束，无法报名");
        }
        
        // 检查是否已经报名
        List<Registration> existRegistrations = registrationMapper.getByContestIdAndUserId(contestId, userId);
        if (existRegistrations != null && !existRegistrations.isEmpty()) {
            throw new BaseException(MessageConstant.REGISTRATION_EXIST);
        }
        
        // 上传附件
        String attachmentPath = null;
        if (attachment != null && !attachment.isEmpty()) {
            try {
                // 生成文件名
                String originalFilename = attachment.getOriginalFilename();
                String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
                String fileName = UUID.randomUUID().toString() + suffix;
                
                // 保存文件
                String uploadPath = "uploads/attachments/";
                File dir = new File(uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                
                File file = new File(dir, fileName);
                attachment.transferTo(file);
                
                attachmentPath = uploadPath + fileName;
            } catch (IOException e) {
                log.error("文件上传失败：", e);
                throw new BaseException(MessageConstant.UPLOAD_FAILED);
            }
        }
        
        // 创建报名记录
        Registration registration = Registration.builder()
                .contestId(contestId)
                .userId(userId)
                .teamName(registrationDTO.getTeamName())
                .attachmentPath(attachmentPath)
                .status("pending")
                .build();
        
        registrationMapper.insert(registration);
        
        // 保存团队成员
        if (registrationDTO.getMembers() != null && !registrationDTO.getMembers().isEmpty()) {
            List<TeamMember> teamMembers = new ArrayList<>();
            for (Long memberId : registrationDTO.getMembers()) {
                // 检查成员是否存在
                User member = userMapper.getById(memberId);
                if (member == null) {
                    continue;
                }
                
                TeamMember teamMember = TeamMember.builder()
                        .registrationId(registration.getId())
                        .memberUserId(memberId)
                        .build();
                
                teamMembers.add(teamMember);
            }
            
            if (!teamMembers.isEmpty()) {
                teamMemberMapper.batchInsert(teamMembers);
            }
        }
        
        return registration.getId();
    }

    /**
     * 获取报名详情
     * @param registrationId 报名ID
     * @return 报名详情
     */
    @Override
    public RegistrationVO getRegistrationDetail(Long registrationId) {
        // 查询报名记录
        Registration registration = registrationMapper.getById(registrationId);
        if (registration == null) {
            throw new BaseException(MessageConstant.REGISTRATION_NOT_FOUND);
        }
        
        // 查询竞赛
        Contest contest = contestMapper.getById(registration.getContestId());
        
        // 查询队长
        User leader = userMapper.getById(registration.getUserId());
        
        // 查询团队成员
        List<TeamMember> teamMembers = teamMemberMapper.listByRegistrationId(registrationId);
        List<TeamMemberVO> teamMemberVOS = new ArrayList<>();
        
        for (TeamMember teamMember : teamMembers) {
            User member = userMapper.getById(teamMember.getMemberUserId());
            if (member == null) {
                continue;
            }
            
            TeamMemberVO teamMemberVO = TeamMemberVO.builder()
                    .userId(member.getId())
                    .username(member.getUsername())
                    .email(member.getEmail())
                    .phone(member.getPhone())
                    .build();
            
            teamMemberVOS.add(teamMemberVO);
        }
        
        // 构建报名VO
        RegistrationVO registrationVO = RegistrationVO.builder()
                .registrationId(registration.getId())
                .contestId(registration.getContestId())
                .contestTitle(contest != null ? contest.getTitle() : "")
                .userId(registration.getUserId())
                .leaderName(leader != null ? leader.getUsername() : "")
                .teamName(registration.getTeamName())
                .members(teamMemberVOS)
                .attachmentPath(registration.getAttachmentPath())
                .status(registration.getStatus())
                .createdTime(registration.getCreatedTime())
                .build();
        
        return registrationVO;
    }

    /**
     * 查询竞赛报名列表
     * @param contestId 竞赛ID
     * @return 报名列表
     */
    @Override
    public List<RegistrationVO> listRegistrations(Long contestId) {
        // 查询报名记录
        List<Registration> registrations = registrationMapper.listByContestId(contestId);
        
        List<RegistrationVO> registrationVOS = new ArrayList<>();
        for (Registration registration : registrations) {
            // 查询队长
            User leader = userMapper.getById(registration.getUserId());
            
            RegistrationVO registrationVO = RegistrationVO.builder()
                    .registrationId(registration.getId())
                    .contestId(registration.getContestId())
                    .userId(registration.getUserId())
                    .leaderName(leader != null ? leader.getUsername() : "")
                    .teamName(registration.getTeamName())
                    .attachmentPath(registration.getAttachmentPath())
                    .status(registration.getStatus())
                    .createdTime(registration.getCreatedTime())
                    .build();
            
            registrationVOS.add(registrationVO);
        }
        
        return registrationVOS;
    }

    /**
     * 审核报名
     * @param registrationId 报名ID
     * @param status 状态
     * @return 是否成功
     */
    @Override
    public boolean auditRegistration(Long registrationId, String status) {
        // 查询报名记录
        Registration registration = registrationMapper.getById(registrationId);
        if (registration == null) {
            throw new BaseException(MessageConstant.REGISTRATION_NOT_FOUND);
        }
        
        // 更新状态
        registrationMapper.updateStatus(registrationId, status);
        
        return true;
    }

    /**
     * 获取所有活跃的竞赛
     * @return 竞赛列表
     */
    @Override
    public List<ContestVO> getActiveContests() {
        List<Contest> contests = contestMapper.listByStatus("ongoing");
        if (contests == null || contests.isEmpty()) {
            return new ArrayList<>();
        }
        
        return contests.stream()
                .map(this::convertToContestVO)
                .toList();
    }
    
    /**
     * 根据学生ID获取参加的竞赛
     * @param studentId 学生ID
     * @return 竞赛列表
     */
    @Override
    public List<ContestVO> getContestsByStudentId(Long studentId) {
        // 先获取学生报名的所有竞赛ID
        List<Long> contestIds = registrationMapper.getContestIdsByStudentId(studentId);
        if (contestIds == null || contestIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 获取竞赛详情
        List<ContestVO> result = new ArrayList<>();
        for (Long contestId : contestIds) {
            Contest contest = contestMapper.getById(contestId);
            if (contest != null) {
                result.add(convertToContestVO(contest));
            }
        }
        
        return result;
    }
    
    /**
     * 将Contest转换为ContestVO
     * @param contest 竞赛实体
     * @return 竞赛VO
     */
    private ContestVO convertToContestVO(Contest contest) {
        ContestVO vo = new ContestVO();
        vo.setContestId(contest.getId());
        vo.setTitle(contest.getTitle());
        vo.setDescription(contest.getDescription());
        vo.setStartTime(contest.getStartTime());
        vo.setEndTime(contest.getEndTime());
        vo.setRules(contest.getRules());
        vo.setAwards(contest.getAwards());
        vo.setCreatorId(contest.getCreatorId());
        vo.setStatus(contest.getStatus());
        vo.setCreatedTime(contest.getCreatedTime());
        
        // 获取创建者信息
        User creator = userMapper.getById(contest.getCreatorId());
        if (creator != null) {
            vo.setCreatorName(creator.getUsername());
        }
        
        return vo;
    }

    @Override
    public Integer countContests() {
        return contestMapper.countContests();
    }

    @Override
    public Integer countContestsByStatus(String status) {
        return contestMapper.countContestsByStatus(status);
    }

    @Override
    public ContestStatisticsVO getContestStatistics(Long contestId) {
        // 查询竞赛信息
        Contest contest = contestMapper.getById(contestId);
        if (contest == null) {
            throw new BaseException(MessageConstant.CONTEST_NOT_FOUND);
        }
        
        ContestStatisticsVO statistics = new ContestStatisticsVO();
        statistics.setContestId(contest.getId());
        statistics.setTitle(contest.getTitle());
        statistics.setStatus(contest.getStatus());
        
        // 转换日期类型
        if (contest.getStartTime() != null) {
            statistics.setStartTime(Date.from(contest.getStartTime().atZone(ZoneId.systemDefault()).toInstant()));
        }
        if (contest.getEndTime() != null) {
            statistics.setEndTime(Date.from(contest.getEndTime().atZone(ZoneId.systemDefault()).toInstant()));
        }
        
        statistics.setCreatorId(contest.getCreatorId());
        
        // 获取创建者信息
        User creator = userMapper.getById(contest.getCreatorId());
        if (creator != null) {
            statistics.setCreatorName(creator.getUsername());
        }
        
        // 获取报名人数
        statistics.setRegistrationCount(registrationMapper.countByContestId(contestId));
        
        // 获取提交数量
        Integer submissionCount = submissionMapper.countByContestId(contestId);
        statistics.setSubmissionCount(submissionCount);
        
        // 获取已评分提交数量
        Integer scoredCount = submissionMapper.countByContestIdAndStatus(contestId, 1); // 状态1表示已评分
        statistics.setScoredSubmissionCount(scoredCount);
        
        // 获取待评分提交数量
        Integer pendingCount = submissionMapper.countByContestIdAndStatus(contestId, 0); // 状态0表示待评分
        statistics.setPendingSubmissionCount(pendingCount);
        
        // 获取评分统计
        if (scoredCount > 0) {
            // 获取平均分
            Double avgScore = scoreMapper.getAverageScoreByContestId(contestId);
            statistics.setAverageScore(avgScore);
            
            // 获取最高分
            Integer highestScore = scoreMapper.getHighestScoreByContestId(contestId);
            statistics.setHighestScore(highestScore);
            
            // 获取最低分
            Integer lowestScore = scoreMapper.getLowestScoreByContestId(contestId);
            statistics.setLowestScore(lowestScore);
            
            // 获取分数分布
            Map<String, Integer> distribution = new HashMap<>();
            distribution.put("90-100", scoreMapper.countScoresByRange(contestId, 90, 100));
            distribution.put("80-89", scoreMapper.countScoresByRange(contestId, 80, 89));
            distribution.put("70-79", scoreMapper.countScoresByRange(contestId, 70, 79));
            distribution.put("60-69", scoreMapper.countScoresByRange(contestId, 60, 69));
            distribution.put("0-59", scoreMapper.countScoresByRange(contestId, 0, 59));
            statistics.setScoreDistribution(distribution);
        }
        
        return statistics;
    }

    @Override
    public List<Map<String, Object>> getParticipationStatistics() {
        List<Map<String, Object>> statistics = new ArrayList<>();
        
        // 获取所有竞赛
        List<Contest> contests = contestMapper.list(null);  // 使用list方法，传入null表示不筛选状态
        
        for (Contest contest : contests) {
            Map<String, Object> contestStats = new HashMap<>();
            Long contestId = contest.getId();  // 使用getId()而不是getContestId()
            
            // 添加竞赛基本信息
            contestStats.put("contestId", contestId);
            contestStats.put("contestName", contest.getTitle());
            
            // 获取报名人数
            int registrationCount = registrationMapper.countByContestId(contestId);
            contestStats.put("registrationCount", registrationCount);
            
            // 获取提交数
            int submissionCount = submissionMapper.countByContestId(contestId);
            
            // 计算提交率
            double submissionRate = registrationCount > 0 ? 
                (double) submissionCount / registrationCount : 0;
            contestStats.put("submissionRate", Math.round(submissionRate * 100) / 100.0);
            
            // 添加其他统计数据
            contestStats.put("submissionCount", submissionCount);
            contestStats.put("status", contest.getStatus());
            
            statistics.add(contestStats);
        }
        
        return statistics;
    }
} 