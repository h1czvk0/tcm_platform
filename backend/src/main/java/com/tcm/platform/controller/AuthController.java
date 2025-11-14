package com.tcm.platform.controller;

import com.tcm.platform.common.Result;
import com.tcm.platform.dto.AdminLoginDTO;
import com.tcm.platform.dto.UserLoginDTO;
import com.tcm.platform.dto.UserRegisterDTO;
import com.tcm.platform.service.AdministratorService;
import com.tcm.platform.service.UserService;
import com.tcm.platform.vo.LoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@Api(tags = "认证接口")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AdministratorService administratorService;

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public Result<Void> register(@Validated @RequestBody UserRegisterDTO registerDTO) {
        userService.register(registerDTO);
        return Result.success("注册成功", null);
    }

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result<LoginVO> login(@Validated @RequestBody UserLoginDTO loginDTO) {
        LoginVO loginVO = userService.login(loginDTO);
        return Result.success("登录成功", loginVO);
    }

    @ApiOperation("管理员登录")
    @PostMapping("/admin/login")
    public Result<LoginVO> adminLogin(@Validated @RequestBody AdminLoginDTO loginDTO) {
        LoginVO loginVO = administratorService.login(loginDTO);
        return Result.success("登录成功", loginVO);
    }
}
