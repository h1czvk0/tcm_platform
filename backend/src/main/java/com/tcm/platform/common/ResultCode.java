package com.tcm.platform.common;

import lombok.Getter;

/**
 * 响应状态码枚举
 */
@Getter
public enum ResultCode {
    
    // 通用状态码
    SUCCESS(200, "操作成功"),
    ERROR(500, "操作失败"),
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    
    // 用户相关
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_ALREADY_EXISTS(1002, "用户已存在"),
    PASSWORD_ERROR(1003, "密码错误"),
    USER_DISABLED(1004, "用户已被禁用"),
    
    // 管理员相关
    ADMIN_NOT_FOUND(2001, "管理员不存在"),
    ADMIN_PASSWORD_ERROR(2002, "管理员密码错误"),
    
    // Token相关
    TOKEN_INVALID(3001, "Token无效"),
    TOKEN_EXPIRED(3002, "Token已过期"),
    TOKEN_MISSING(3003, "Token缺失"),
    
    // 业务相关
    MEDICINE_NOT_FOUND(4001, "中药不存在"),
    DIET_NOT_FOUND(4002, "药膳不存在"),
    QUESTION_NOT_FOUND(4003, "题目不存在"),
    CONSULTATION_NOT_FOUND(4004, "问诊记录不存在"),
    REPORT_NOT_FOUND(4005, "体质报告不存在"),
    ALREADY_COLLECTED(4006, "已收藏过该项目"),
    COLLECTION_NOT_FOUND(4007, "收藏记录不存在");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
