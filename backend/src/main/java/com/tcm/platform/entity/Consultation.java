package com.tcm.platform.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 智能问诊实体类
 */
@Data
@TableName("consultation")
public class Consultation implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 问诊ID
     */
    @TableId(value = "consultation_id", type = IdType.AUTO)
    private Long consultationId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 舌象照片URL
     */
    private String tongueImageUrl;

    /**
     * 问诊日期
     */
    private LocalDateTime consultationDate;

    /**
     * 状态：IN_PROGRESS-进行中，COMPLETED-已完成，CANCELED-已取消
     */
    private String status;

    /**
     * 版本号
     */
    private Integer version;

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
