package com.tcm.platform.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 错题集实体类
 */
@Data
@TableName("wrong_question")
public class WrongQuestion implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 错题ID
     */
    @TableId(value = "wrong_id", type = IdType.AUTO)
    private Long wrongId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 题目ID
     */
    private Long questionId;

    /**
     * 错误次数
     */
    private Integer wrongCount;

    /**
     * 最后错误时间
     */
    private LocalDateTime lastWrongTime;

    /**
     * 用户笔记
     */
    private String note;

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
