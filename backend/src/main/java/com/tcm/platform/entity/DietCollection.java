package com.tcm.platform.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 药膳收藏实体类
 */
@Data
@TableName("diet_collection")
public class DietCollection implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 收藏ID
     */
    @TableId(value = "diet_collection_id", type = IdType.AUTO)
    private Long dietCollectionId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 药膳ID
     */
    private Long dietId;

    /**
     * 收藏时间
     */
    private LocalDateTime collectionTime;

    /**
     * 收藏备注
     */
    private String note;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
