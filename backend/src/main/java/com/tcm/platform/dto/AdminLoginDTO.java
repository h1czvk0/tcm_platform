package com.tcm.platform.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * 管理员登录DTO
 */
@Data
public class AdminLoginDTO {
    
    @NotBlank(message = "管理员名不能为空")
    private String adminName;
    
    @NotBlank(message = "密码不能为空")
    private String adminPassword;
}
