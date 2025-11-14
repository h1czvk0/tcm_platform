package com.tcm.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tcm.platform.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    /**
     * 根据用户名查询用户
     */
    User selectByUsername(@Param("username") String username);
    
    /**
     * 根据微信OpenID查询用户
     */
    User selectByWechatOpenid(@Param("openid") String openid);
    
    /**
     * 分页查询用户列表
     */
    IPage<User> selectUserPage(Page<User> page, @Param("keyword") String keyword, 
                                @Param("status") String status);
}
