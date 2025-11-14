package com.tcm.platform.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 药膳实体类
 */
@Data
@TableName("herbal_diet")
public class HerbalDiet implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 药膳ID
     */
    @TableId(value = "diet_id", type = IdType.AUTO)
    private Long dietId;

    /**
     * 药膳名称
     */
    private String dietName;

    /**
     * 药膳描述
     */
    private String description;

    /**
     * 功效
     */
    private String efficacy;

    /**
     * 食材列表(JSON格式)
     */
    private String ingredients;

    /**
     * 制作步骤(JSON格式)
     */
    private String preparationSteps;

    /**
     * 烹饪时间(分钟)
     */
    private Integer cookingTime;

    /**
     * 难度等级：EASY-简单，MEDIUM-中等，HARD-困难
     */
    private String difficultyLevel;

    /**
     * 推荐季节
     */
    private String season;

    /**
     * 药膳图片URL
     */
    private String imageUrl;

    /**
     * 浏览次数
     */
    private Integer viewsCount;

    /**
     * 收藏次数
     */
    private Integer collectionCount;

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
