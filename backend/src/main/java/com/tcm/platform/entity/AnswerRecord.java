package com.tcm.platform.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 答题记录实体类
 */
@Data
@TableName("answer_record")
public class AnswerRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
    @TableId(value = "record_id", type = IdType.AUTO)
    private Long recordId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 题目ID
     */
    private Long questionId;

    /**
     * 用户答案
     */
    private String userAnswer;

    /**
     * 是否正确
     */
    private Boolean isCorrect;

    /**
     * 答题时间
     */
    private LocalDateTime answerTime;

    /**
     * 花费时间(秒)
     */
    private Integer spendTime;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
