package com.tcm.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tcm.platform.common.PageResult;
import com.tcm.platform.dto.UserLoginDTO;
import com.tcm.platform.dto.UserRegisterDTO;
import com.tcm.platform.entity.User;
import com.tcm.platform.vo.LoginVO;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {
    
    /**
     * 用户注册
     */
    void register(UserRegisterDTO registerDTO);
    
    /**
     * 用户登录
     */
    LoginVO login(UserLoginDTO loginDTO);
    
    /**
     * 根据用户名查询用户
     */
    User getUserByUsername(String username);
    
    /**
     * 更新最后登录时间
     */
    void updateLastLoginTime(Long userId);
    
    /**
     * 分页查询用户列表
     */
    PageResult<User> getUserPage(Long current, Long size, String keyword, String status);
    
    /**
     * 修改用户状态
     */
    void updateUserStatus(Long userId, String status);
}
