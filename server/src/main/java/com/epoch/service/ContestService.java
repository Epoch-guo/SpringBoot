package com.epoch.service;

import com.epoch.dao.ContestDTO;
import com.epoch.dao.RegistrationDTO;
import com.epoch.entity.Contest;
import com.epoch.entity.Registration;
import com.epoch.result.PageResult;
import com.epoch.vo.ContestListVO;
import com.epoch.vo.ContestStatisticsVO;
import com.epoch.vo.ContestVO;
import com.epoch.vo.RegistrationVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 竞赛服务接口
 */
public interface ContestService {
    
    /**
     * 创建竞赛
     * @param contestDTO 竞赛信息
     * @param creatorId 创建者ID
     * @return 竞赛ID
     */
    Long createContest(ContestDTO contestDTO, Long creatorId);
    
    /**
     * 更新竞赛
     * @param id 竞赛ID
     * @param contestDTO 竞赛信息
     * @return 是否成功
     */
    boolean updateContest(Long id, ContestDTO contestDTO);
    
    /**
     * 获取竞赛详情
     * @param id 竞赛ID
     * @return 竞赛详情
     */
    ContestVO getContestDetail(Long id);
    
    /**
     * 分页查询竞赛列表
     * @param page 页码
     * @param pageSize 每页数量
     * @param status 状态
     * @return 分页结果
     */
    PageResult pageQuery(int page, int pageSize, String status);
    
    /**
     * 查询教师创建的竞赛列表
     * @param creatorId 创建者ID
     * @return 竞赛列表
     */
    List<ContestListVO> listByCreator(Long creatorId);
    
    /**
     * 更新竞赛状态
     * @param id 竞赛ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateStatus(Long id, String status);
    
    /**
     * 删除竞赛
     * @param id 竞赛ID
     * @return 是否成功
     */
    boolean deleteContest(Long id);
    
    /**
     * 竞赛报名
     * @param contestId 竞赛ID
     * @param registrationDTO 报名信息
     * @param userId 用户ID
     * @param attachment 附件
     * @return 报名ID
     */
    Long register(Long contestId, RegistrationDTO registrationDTO, Long userId, MultipartFile attachment);
    
    /**
     * 获取报名详情
     * @param registrationId 报名ID
     * @return 报名详情
     */
    RegistrationVO getRegistrationDetail(Long registrationId);
    
    /**
     * 查询竞赛报名列表
     * @param contestId 竞赛ID
     * @return 报名列表
     */
    List<RegistrationVO> listRegistrations(Long contestId);
    
    /**
     * 审核报名
     * @param registrationId 报名ID
     * @param status 状态
     * @return 是否成功
     */
    boolean auditRegistration(Long registrationId, String status);
    
    /**
     * 获取所有活跃的竞赛
     * @return 竞赛列表
     */
    List<ContestVO> getActiveContests();
    
    /**
     * 根据学生ID获取参加的竞赛
     * @param studentId 学生ID
     * @return 竞赛列表
     */
    List<ContestVO> getContestsByStudentId(Long studentId);
    
    /**
     * 获取竞赛总数
     * @return 竞赛总数
     */
    Integer countContests();

    /**
     * 按状态统计竞赛数量
     * @param status 竞赛状态
     * @return 符合状态的竞赛数量
     */
    Integer countContestsByStatus(String status);
    
    /**
     * 获取竞赛统计数据
     * @param contestId 竞赛ID
     * @return 竞赛统计数据
     */
    ContestStatisticsVO getContestStatistics(Long contestId);
    
    /**
     * 获取竞赛参与度统计
     * @return 各竞赛参与度统计数据
     */
    List<Map<String, Object>> getParticipationStatistics();
    
    /**
     * 获取进行中的竞赛列表（首页展示）
     * @param limit 获取条数
     * @return 竞赛列表
     */
    List<ContestListVO> getOngoingContests(Integer limit);
    
    /**
     * 获取所有竞赛数量
     * @return 竞赛总数
     */
    int countAllContests();
} 