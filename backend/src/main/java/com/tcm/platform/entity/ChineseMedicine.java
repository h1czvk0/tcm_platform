package com.tcm.platform.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 中药材实体类
 */
@Data
@TableName("chinese_medicine")
public class ChineseMedicine implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 中药ID
     */
    @TableId(value = "medicine_id", type = IdType.AUTO)
    private Long medicineId;

    /**
     * 中药名称
     */
    private String medicineName;

    /**
     * 别名
     */
    private String medicineAlias;

    /**
     * 性质（温、热、凉、寒、平）
     */
    private String nature;

    /**
     * 味道（酸、苦、甘、辛、咸）
     */
    private String flavor;

    /**
     * 归经
     */
    private String meridians;

    /**
     * 功效描述
     */
    private String functions;

    /**
     * 用法用量
     */
    private String usage;

    /**
     * 禁忌事项
     */
    private String contraindications;

    /**
     * 中药图片URL
     */
    private String imageUrl;

    /**
     * 信息来源
     */
    private String source;

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
