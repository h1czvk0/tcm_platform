package com.tcm.platform.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 管理员实体类
 */
@Data
@TableName("administrator")
public class Administrator implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 管理员ID
     */
    @TableId(value = "admin_id", type = IdType.AUTO)
    private Long adminId;

    /**
     * 管理员名
     */
    private String adminName;

    /**
     * 管理员密码
     */
    private String adminPassword;

    /**
     * 管理员角色
     */
    private String adminRole;

    /**
     * 权限级别
     */
    private Integer permissionLevel;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 是否激活
     */
    private Boolean isActive;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
