package com.tcm.platform.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 知识题目实体类
 */
@Data
@TableName("knowledge_question")
public class KnowledgeQuestion implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 题目ID
     */
    @TableId(value = "question_id", type = IdType.AUTO)
    private Long questionId;

    /**
     * 题目内容
     */
    private String questionContent;

    /**
     * 题型：SINGLE-单选，MULTIPLE-多选，JUDGE-判断
     */
    private String questionType;

    /**
     * 分类
     */
    private String category;

    /**
     * 难度等级：EASY-简单，MEDIUM-中等，HARD-困难
     */
    private String difficulty;

    /**
     * A选项
     */
    private String optionA;

    /**
     * B选项
     */
    private String optionB;

    /**
     * C选项
     */
    private String optionC;

    /**
     * D选项
     */
    private String optionD;

    /**
     * 正确答案
     */
    private String correctAnswer;

    /**
     * 答案解析
     */
    private String explanation;

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
