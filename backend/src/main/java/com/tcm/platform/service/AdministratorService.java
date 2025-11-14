package com.tcm.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tcm.platform.dto.AdminLoginDTO;
import com.tcm.platform.entity.Administrator;
import com.tcm.platform.vo.LoginVO;

/**
 * 管理员服务接口
 */
public interface AdministratorService extends IService<Administrator> {
    
    /**
     * 管理员登录
     */
    LoginVO login(AdminLoginDTO loginDTO);
    
    /**
     * 根据管理员名查询
     */
    Administrator getAdminByName(String adminName);
}
