package com.epoch.mapper;

import com.epoch.entity.Contest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 竞赛Mapper接口
 */
@Mapper
public interface ContestMapper {
    
    /**
     * 保存竞赛
     * @param contest 竞赛对象
     * @return 影响行数
     */
    int insert(Contest contest);
    
    /**
     * 更新竞赛
     * @param contest 竞赛对象
     * @return 影响行数
     */
    int update(Contest contest);
    
    /**
     * 根据ID查询竞赛
     * @param id 竞赛ID
     * @return 竞赛对象
     */
    Contest getById(Long id);
    
    /**
     * 查询竞赛列表
     * @param status 状态
     * @return 竞赛列表
     */
    List<Contest> list(@Param("status") String status);
    
    /**
     * 根据创建者ID查询竞赛列表
     * @param creatorId 创建者ID
     * @return 竞赛列表
     */
    List<Contest> listByCreatorId(Long creatorId);
    
    /**
     * 根据创建者ID和状态查询竞赛列表
     * @param creatorId 创建者ID
     * @param status 状态
     * @return 竞赛列表
     */
    List<Contest> listByCreatorIdAndStatus(@Param("creatorId") Long creatorId, @Param("status") String status);
    
    /**
     * 更新竞赛状态
     * @param id 竞赛ID
     * @param status 状态
     * @return 影响行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") String status);
    
    /**
     * 删除竞赛
     * @param id 竞赛ID
     * @return 影响行数
     */
    int deleteById(Long id);
    
    /**
     * 根据状态查询竞赛列表
     * @param status 状态
     * @return 竞赛列表
     */
    List<Contest> listByStatus(String status);
    
    /**
     * 获取所有竞赛
     */
    List<Contest> listAll();
    
    /**
     * 统计竞赛总数
     */
    Integer countContests();
    
    /**
     * 按状态统计竞赛数量
     */
    Integer countContestsByStatus(@Param("status") String status);
} 