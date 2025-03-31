package com.epoch.mapper;

import com.epoch.entity.Registration;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 报名Mapper接口
 */
@Mapper
public interface RegistrationMapper {
    
    /**
     * 保存报名
     * @param registration 报名对象
     * @return 影响行数
     */
    int insert(Registration registration);
    
    /**
     * 更新报名
     * @param registration 报名对象
     * @return 影响行数
     */
    int update(Registration registration);
    
    /**
     * 根据ID查询报名
     * @param id 报名ID
     * @return 报名对象
     */
    Registration getById(Long id);
    
    /**
     * 根据竞赛ID和用户ID查询报名列表
     * @param contestId 竞赛ID
     * @param userId 用户ID
     * @return 报名列表
     */
    List<Registration> getByContestIdAndUserId(@Param("contestId") Long contestId, @Param("userId") Long userId);
    
    /**
     * 根据竞赛ID查询报名列表
     * @param contestId 竞赛ID
     * @return 报名列表
     */
    List<Registration> listByContestId(Long contestId);
    
    /**
     * 根据用户ID查询报名列表
     * @param userId 用户ID
     * @return 报名列表
     */
    List<Registration> listByUserId(Long userId);
    
    /**
     * 查询所有团队
     * @return 团队列表
     */
    List<Registration> listAllTeams();
    
    /**
     * 更新报名状态
     * @param id 报名ID
     * @param status 状态
     * @return 影响行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") String status);
    
    /**
     * 统计竞赛报名人数
     * @param contestId 竞赛ID
     * @return 报名人数
     */
    int countByContestId(Long contestId);
    
    /**
     * 删除报名
     * @param id 报名ID
     * @return 影响行数
     */
    int deleteById(Long id);
    
    /**
     * 获取竞赛队长ID列表
     * @param contestId 竞赛ID
     * @return 队长ID列表
     */
    List<Long> listLeaderIdsByContestId(Long contestId);
    
    /**
     * 获取学生报名的所有竞赛ID
     * @param studentId 学生ID
     * @return 竞赛ID列表
     */
    List<Long> getContestIdsByStudentId(Long studentId);
    
    /**
     * 统计所有报名记录数量
     * @return 报名记录总数
     */
    Integer countRegistrations();
} 