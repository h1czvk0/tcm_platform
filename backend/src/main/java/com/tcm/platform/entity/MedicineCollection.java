package com.tcm.platform.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 中药收藏实体类
 */
@Data
@TableName("medicine_collection")
public class MedicineCollection implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 收藏ID
     */
    @TableId(value = "collection_id", type = IdType.AUTO)
    private Long collectionId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 中药ID
     */
    private Long medicineId;

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
