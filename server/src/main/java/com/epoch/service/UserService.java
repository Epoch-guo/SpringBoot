package com.epoch.service;

import com.epoch.dao.LoginDTO;
import com.epoch.dao.RegisterDTO;
import com.epoch.dao.UserQueryDTO;
import com.epoch.dao.UserUpdateDTO;
import com.epoch.entity.User;
import com.epoch.vo.LoginVO;
import com.epoch.vo.StudentVO;
import com.epoch.vo.UserPageVO;
import com.epoch.vo.UserVO;

import java.util.List;

/**
 * 用户服务接口
 */
public interface UserService {
    
    /**
     * 用户登录
     * @param loginDTO 登录信息
     * @return 登录响应
     */
    LoginVO login(LoginDTO loginDTO);
    
    /**
     * 用户退出登录
     * @return 是否成功
     */
    boolean logout();
    
    /**
     * 学生注册
     * @param registerDTO 注册信息
     * @return 用户ID
     */
    Long register(RegisterDTO registerDTO);
    
    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户对象
     */
    User getByUsername(String username);
    
    /**
     * 根据ID查询用户
     * @param id 用户ID
     * @return 用户对象
     */
    User getById(Long id);
    
    /**
     * 获取学生列表
     * @param contestId 竞赛ID，可为null
     * @return 学生列表
     */
    List<StudentVO> listStudents(Long contestId);
    
    /**
     * 分页查询用户列表
     * @param queryDTO 查询条件
     * @return 用户分页列表
     */
    UserPageVO pageQuery(UserQueryDTO queryDTO);
    
    /**
     * 获取用户详情
     * @param id 用户ID
     * @return 用户详情
     */
    UserVO getUserDetail(Long id);
    
    /**
     * 更新用户信息
     * @param updateDTO 更新信息
     * @return 是否成功
     */
    boolean updateUser(UserUpdateDTO updateDTO);
    
    /**
     * 删除用户
     * @param id 用户ID
     * @return 是否成功
     */
    boolean deleteUser(Long id);
    
    /**
     * 重置用户密码
     * @param id 用户ID
     * @return 新密码
     */
    String resetPassword(Long id);
    
    /**
     * 获取所有学生用户
     * @return 学生用户列表
     */
    List<UserVO> getAllStudents();

    /**
     * 获取用户总数
     * @return 用户总数
     */
    Integer countUsers();

    /**
     * 按角色统计用户数量
     * @param role 用户角色
     * @return 符合角色的用户数量
     */
    Integer countUsersByRole(String role);
} 