package com.tcm.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tcm.platform.common.ResultCode;
import com.tcm.platform.dto.AdminLoginDTO;
import com.tcm.platform.entity.Administrator;
import com.tcm.platform.exception.BusinessException;
import com.tcm.platform.mapper.AdministratorMapper;
import com.tcm.platform.service.AdministratorService;
import com.tcm.platform.utils.JwtUtil;
import com.tcm.platform.utils.Md5Util;
import com.tcm.platform.vo.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 管理员服务实现类
 */
@Service
public class AdministratorServiceImpl extends ServiceImpl<AdministratorMapper, Administrator> 
        implements AdministratorService {

    @Autowired
    private AdministratorMapper administratorMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public LoginVO login(AdminLoginDTO loginDTO) {
        // 查询管理员
        Administrator admin = administratorMapper.selectByAdminName(loginDTO.getAdminName());
        if (admin == null) {
            throw new BusinessException(ResultCode.ADMIN_NOT_FOUND);
        }

        // 验证密码
        if (!Md5Util.verify(loginDTO.getAdminPassword(), admin.getAdminPassword())) {
            throw new BusinessException(ResultCode.ADMIN_PASSWORD_ERROR);
        }

        // 更新最后登录时间
        admin.setLastLoginTime(LocalDateTime.now());
        administratorMapper.updateById(admin);

        // 生成Token
        String token = jwtUtil.generateToken(admin.getAdminId(), admin.getAdminName(), "ADMIN");

        // 构建返回结果
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);
        loginVO.setUserId(admin.getAdminId());
        loginVO.setUsername(admin.getAdminName());
        loginVO.setNickname(admin.getAdminName());
        loginVO.setUserType("ADMIN");

        return loginVO;
    }

    @Override
    public Administrator getAdminByName(String adminName) {
        return administratorMapper.selectByAdminName(adminName);
    }
}
