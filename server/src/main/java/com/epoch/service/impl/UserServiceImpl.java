package com.epoch.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.epoch.constant.MessageConstant;
import com.epoch.constant.PasswordErrorException;
import com.epoch.dao.LoginDTO;
import com.epoch.dao.RegisterDTO;
import com.epoch.dao.UserQueryDTO;
import com.epoch.dao.UserUpdateDTO;
import com.epoch.entity.User;
import com.epoch.exception.AccountNotFoundException;
import com.epoch.exception.BadCredentialsException;
import com.epoch.mapper.RegistrationMapper;
import com.epoch.mapper.UserMapper;
import com.epoch.service.UserService;
import com.epoch.vo.LoginVO;
import com.epoch.vo.StudentVO;
import com.epoch.vo.UserPageVO;
import com.epoch.vo.UserVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户服务实现类
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RegistrationMapper registrationMapper;

    /**
     * 用户登录
     * @param loginDTO 登录信息
     * @return 登录响应
     */
    @Override
    public LoginVO login(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        
        // 根据用户名查询用户
        User user = userMapper.getByUsername(username);
        if (user == null) {
            // 尝试通过学号查询（在当前数据库结构中，学号就是username）
            user = userMapper.getByStudentId(username);
            if (user == null) {
                throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
            }
        }
        
        // 密码比对
        String encryptedPassword = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        if (!encryptedPassword.equals(user.getPassword())) {
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }
        
        // 判断账号状态
        if ("inactive".equals(user.getStatus())) {
            throw new BadCredentialsException(MessageConstant.ACCOUNT_LOCKED);
        }
        
        // 登录成功，生成token
        StpUtil.login(user.getId());
        String token = StpUtil.getTokenValue();
        
        // 获取权限列表（通过StpInterfaceImpl自动获取）
        List<String> permissions = StpUtil.getPermissionList();
        
        // 构建登录响应
        return LoginVO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .token(token)
                .permissions(permissions)
                .build();
    }

    /**
     * 用户退出登录
     * @return 是否成功
     */
    @Override
    public boolean logout() {
        try {
            // 检查当前是否已登录
            if (StpUtil.isLogin()) {
                // 获取当前登录用户ID
                Long userId = StpUtil.getLoginIdAsLong();
                log.info("用户退出登录：userId = {}", userId);
                
                // 注销登录
                StpUtil.logout();
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("退出登录失败：", e);
            return false;
        }
    }

    /**
     * 学生注册
     * @param registerDTO 注册信息
     * @return 用户ID
     */
    @Override
    public Long register(RegisterDTO registerDTO) {
        // 检查用户名是否已存在
        User existUser = userMapper.getByUsername(registerDTO.getUsername());
        if (existUser != null) {
            throw new BadCredentialsException(MessageConstant.ACCOUNT_FOUND);
        }
        
        // 检查学号是否已存在（在当前数据库结构中，学号就是username）
        existUser = userMapper.getByStudentId(registerDTO.getStudentId());
        if (existUser != null) {
            throw new BadCredentialsException("学号已被注册");
        }
        
        // 创建用户对象
        User user = User.builder()
                .username(registerDTO.getStudentId()) // 使用学号作为用户名
                .password(DigestUtils.md5DigestAsHex(registerDTO.getPassword().getBytes(StandardCharsets.UTF_8)))
                .email(registerDTO.getEmail())
                .phone(registerDTO.getPhone())
                .role("student") // 注册的用户默认为学生角色
                .status("active") // 默认状态为正常
                .build();
        
        // 保存用户
        userMapper.insert(user);
        
        return user.getId();
    }

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户对象
     */
    @Override
    public User getByUsername(String username) {
        return userMapper.getByUsername(username);
    }

    /**
     * 根据ID查询用户
     * @param id 用户ID
     * @return 用户对象
     */
    @Override
    public User getById(Long id) {
        return userMapper.getById(id);
    }
    
    /**
     * 获取学生列表
     * @param contestId 竞赛ID，可为null
     * @return 学生列表
     */
    @Override
    public List<StudentVO> listStudents(Long contestId) {
        List<User> students = userMapper.listStudents();
        List<StudentVO> studentVOList = new ArrayList<>();
        
        // 如果提供了竞赛ID，获取该竞赛的队长ID列表
        Set<Long> leaderIds = new HashSet<>();
        if (contestId != null) {
            List<Long> leaders = registrationMapper.listLeaderIdsByContestId(contestId);
            leaderIds.addAll(leaders);
        }
        
        for (User student : students) {
            StudentVO vo = new StudentVO();
            vo.setId(student.getId());
            vo.setUsername(student.getUsername());
            vo.setEmail(student.getEmail());
            vo.setPhone(student.getPhone());
            
            // 标记是否为队长
            vo.setIsLeader(leaderIds.contains(student.getId()));
            
            studentVOList.add(vo);
        }
        
        return studentVOList;
    }
    
    /**
     * 根据角色获取权限列表
     * @param role 角色
     * @return 权限列表
     */
    private List<String> getPermissionsByRole(String role) {
        List<String> permissions = new ArrayList<>();
        
        // 根据角色设置不同的权限
        switch (role) {
            case "admin":
                permissions.add("user:manage");
                permissions.add("contest:manage");
                permissions.add("system:manage");
                permissions.add("contest:view");
                break;
            case "teacher":
                permissions.add("contest:create");
                permissions.add("contest:edit");
                permissions.add("contest:view");
                permissions.add("submission:review");
                break;
            case "student":
                permissions.add("contest:view");
                permissions.add("submission:create");
                break;
        }
        
        return permissions;
    }
    
    /**
     * 分页查询用户列表
     * @param queryDTO 查询条件
     * @return 用户分页列表
     */
    @Override
    public UserPageVO pageQuery(UserQueryDTO queryDTO) {
        // 设置分页参数
        PageHelper.startPage(queryDTO.getPage(), queryDTO.getPageSize());
        
        // 查询用户列表
        Page<User> userPage = (Page<User>) userMapper.listByCondition(
                queryDTO.getKeyword(), 
                queryDTO.getRole(), 
                queryDTO.getStatus()
        );
        
        // 转换为VO
        List<UserVO> userVOList = new ArrayList<>();
        for (User user : userPage.getResult()) {
            UserVO userVO = convertToUserVO(user);
            userVOList.add(userVO);
        }
        
        // 构建分页结果
        UserPageVO pageVO = new UserPageVO();
        pageVO.setTotal(userPage.getTotal());
        pageVO.setList(userVOList);
        
        return pageVO;
    }
    
    /**
     * 获取用户详情
     * @param id 用户ID
     * @return 用户详情
     */
    @Override
    public UserVO getUserDetail(Long id) {
        User user = userMapper.getById(id);
        if (user == null) {
            throw new AccountNotFoundException("用户不存在");
        }
        
        return convertToUserVO(user);
    }
    
    /**
     * 更新用户信息
     * @param updateDTO 更新信息
     * @return 是否成功
     */
    @Override
    public boolean updateUser(UserUpdateDTO updateDTO) {
        // 检查用户是否存在
        User existUser = userMapper.getById(updateDTO.getId());
        if (existUser == null) {
            throw new AccountNotFoundException("用户不存在");
        }
        
        // 如果修改了用户名，检查用户名是否已存在
        if (updateDTO.getUsername() != null && !updateDTO.getUsername().equals(existUser.getUsername())) {
            User userWithSameName = userMapper.getByUsername(updateDTO.getUsername());
            if (userWithSameName != null && !userWithSameName.getId().equals(updateDTO.getId())) {
                throw new BadCredentialsException("用户名已存在");
            }
        }
        
        // 更新用户信息
        User user = new User();
        user.setId(updateDTO.getId());
        user.setUsername(updateDTO.getUsername());
        user.setEmail(updateDTO.getEmail());
        user.setPhone(updateDTO.getPhone());
        user.setRole(updateDTO.getRole());
        user.setStatus(updateDTO.getStatus());
        
        userMapper.update(user);
        
        return true;
    }
    
    /**
     * 删除用户
     * @param id 用户ID
     * @return 是否成功
     */
    @Override
    public boolean deleteUser(Long id) {
        // 检查用户是否存在
        User existUser = userMapper.getById(id);
        if (existUser == null) {
            throw new AccountNotFoundException("用户不存在");
        }
        
        // 不允许删除管理员
        if ("admin".equals(existUser.getRole())) {
            throw new BadCredentialsException("不允许删除管理员");
        }
        
        // 删除用户
        userMapper.deleteById(id);
        
        return true;
    }
    
    /**
     * 重置用户密码
     * @param id 用户ID
     * @return 新密码
     */
    @Override
    public String resetPassword(Long id) {
        // 检查用户是否存在
        User existUser = userMapper.getById(id);
        if (existUser == null) {
            throw new AccountNotFoundException("用户不存在");
        }
        
        // 生成随机密码
        String newPassword = generateRandomPassword();
        
        // 加密密码
        String encryptedPassword = DigestUtils.md5DigestAsHex(newPassword.getBytes(StandardCharsets.UTF_8));
        
        // 更新密码
        userMapper.updatePassword(id, encryptedPassword);
        
        return newPassword;
    }

    /**
     * 获取所有学生用户
     * @return 学生用户列表
     */
    @Override
    public List<UserVO> getAllStudents() {
        List<User> students = userMapper.getUsersByRole("student");
        if (students == null || students.isEmpty()) {
            return new ArrayList<>();
        }
        
        return students.stream()
                .map(this::convertToUserVO)
                .toList();
    }
    
    /**
     * 生成随机密码
     * @return 随机密码
     */
    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int index = (int) (Math.random() * chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }
    
    /**
     * 将User转换为UserVO
     * @param user 用户对象
     * @return 用户VO
     */
    private UserVO convertToUserVO(User user) {
        return UserVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .status(user.getStatus())
                .createTime(user.getCreateTime())
                .build();
    }

    @Override
    public Integer countUsers() {
        return userMapper.countUsers();
    }

    @Override
    public Integer countUsersByRole(String role) {
        return userMapper.countByRole(role);
    }
} 