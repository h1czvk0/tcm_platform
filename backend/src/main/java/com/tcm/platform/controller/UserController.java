package com.tcm.platform.controller;

import com.tcm.platform.common.PageResult;
import com.tcm.platform.common.Result;
import com.tcm.platform.entity.User;
import com.tcm.platform.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户控制器
 */
@Api(tags = "用户接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation("获取当前用户信息")
    @GetMapping("/info")
    public Result<User> getUserInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        User user = userService.getById(userId);
        // 隐藏密码
        user.setPassword(null);
        return Result.success(user);
    }

    @ApiOperation("更新用户信息")
    @PutMapping("/update")
    public Result<Void> updateUser(@RequestBody User user, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        user.setUserId(userId);
        user.setPassword(null); // 不允许通过此接口修改密码
        userService.updateById(user);
        return Result.success("更新成功", null);
    }

    @ApiOperation("分页查询用户列表（管理员）")
    @GetMapping("/page")
    public Result<PageResult<User>> getUserPage(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status) {
        PageResult<User> pageResult = userService.getUserPage(current, size, keyword, status);
        return Result.success(pageResult);
    }

    @ApiOperation("修改用户状态（管理员）")
    @PutMapping("/status/{userId}")
    public Result<Void> updateUserStatus(@PathVariable Long userId, 
                                         @RequestParam String status) {
        userService.updateUserStatus(userId, status);
        return Result.success("状态修改成功", null);
    }
}
