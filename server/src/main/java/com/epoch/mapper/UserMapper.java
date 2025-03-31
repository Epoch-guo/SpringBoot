package com.epoch.mapper;

import com.epoch.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper {
    
    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户对象
     */
    User getByUsername(String username);
    
    /**
     * 根据学号查询用户
     * @param studentId 学号
     * @return 用户对象
     */
    User getByStudentId(String studentId);
    
    /**
     * 保存用户
     * @param user 用户对象
     * @return 影响行数
     */
    int insert(User user);
    
    /**
     * 更新用户
     * @param user 用户对象
     * @return 影响行数
     */
    int update(User user);
    
    /**
     * 根据ID查询用户
     * @param id 用户ID
     * @return 用户对象
     */
    User getById(Long id);
    
    /**
     * 查询所有学生ID列表
     * 注意：由于数据库中没有class_id字段，此方法返回所有学生ID
     * @return 学生ID列表
     */
    List<Long> listStudentIdsByClassId(Long classId);
    
    /**
     * 查询所有用户
     * @return 用户列表
     */
    List<User> listAll();
    
    /**
     * 查询所有学生
     * @return 学生列表
     */
    List<User> listStudents();
    
    /**
     * 根据条件查询用户列表
     * @param keyword 关键词
     * @param role 角色
     * @param status 状态
     * @return 用户列表
     */
    List<User> listByCondition(@Param("keyword") String keyword, @Param("role") String role, @Param("status") String status);
    
    /**
     * 更新用户状态
     * @param id 用户ID
     * @param status 状态
     * @return 影响行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") String status);
    
    /**
     * 更新用户角色
     * @param id 用户ID
     * @param role 角色
     * @return 影响行数
     */
    int updateRole(@Param("id") Long id, @Param("role") String role);
    
    /**
     * 更新用户密码
     * @param id 用户ID
     * @param password 密码
     * @return 影响行数
     */
    int updatePassword(@Param("id") Long id, @Param("password") String password);
    
    /**
     * 删除用户
     * @param id 用户ID
     * @return 影响行数
     */
    int deleteById(Long id);
    
    /**
     * 根据角色获取用户
     * @param role 角色
     * @return 用户列表
     */
    List<User> getUsersByRole(String role);

    List<User> listByRole(@Param("role") String role);

    Integer countUsers();

    Integer countByRole(@Param("role") String role);
} 