package com.tcm.platform.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tcm.platform.common.PageResult;
import com.tcm.platform.common.ResultCode;
import com.tcm.platform.dto.UserLoginDTO;
import com.tcm.platform.dto.UserRegisterDTO;
import com.tcm.platform.entity.User;
import com.tcm.platform.exception.BusinessException;
import com.tcm.platform.mapper.UserMapper;
import com.tcm.platform.service.UserService;
import com.tcm.platform.utils.JwtUtil;
import com.tcm.platform.utils.Md5Util;
import com.tcm.platform.vo.LoginVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(UserRegisterDTO registerDTO) {
        // 检查用户名是否已存在
        User existUser = userMapper.selectByUsername(registerDTO.getUsername());
        if (existUser != null) {
            throw new BusinessException(ResultCode.USER_ALREADY_EXISTS);
        }

        // 创建用户
        User user = new User();
        BeanUtils.copyProperties(registerDTO, user);
        user.setPassword(Md5Util.encrypt(registerDTO.getPassword()));
        user.setRegistrationDate(LocalDateTime.now());
        user.setAccountStatus("ACTIVE");
        
        if (user.getNickname() == null || user.getNickname().isEmpty()) {
            user.setNickname("用户" + System.currentTimeMillis());
        }

        userMapper.insert(user);
    }

    @Override
    public LoginVO login(UserLoginDTO loginDTO) {
        // 查询用户
        User user = userMapper.selectByUsername(loginDTO.getUsername());
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        // 检查账户状态
        if (!"ACTIVE".equals(user.getAccountStatus())) {
            throw new BusinessException(ResultCode.USER_DISABLED);
        }

        // 验证密码
        if (!Md5Util.verify(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.PASSWORD_ERROR);
        }

        // 更新最后登录时间
        updateLastLoginTime(user.getUserId());

        // 生成Token
        String token = jwtUtil.generateToken(user.getUserId(), user.getUsername(), "USER");

        // 构建返回结果
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);
        loginVO.setUserId(user.getUserId());
        loginVO.setUsername(user.getUsername());
        loginVO.setNickname(user.getNickname());
        loginVO.setAvatarUrl(user.getAvatarUrl());
        loginVO.setUserType("USER");

        return loginVO;
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public void updateLastLoginTime(Long userId) {
        User user = new User();
        user.setUserId(userId);
        user.setLastLoginDate(LocalDateTime.now());
        userMapper.updateById(user);
    }

    @Override
    public PageResult<User> getUserPage(Long current, Long size, String keyword, String status) {
        Page<User> page = new Page<>(current, size);
        IPage<User> userPage = userMapper.selectUserPage(page, keyword, status);
        return new PageResult<>(userPage.getCurrent(), userPage.getSize(), 
                                userPage.getTotal(), userPage.getRecords());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserStatus(Long userId, String status) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        user.setAccountStatus(status);
        userMapper.updateById(user);
    }
}
