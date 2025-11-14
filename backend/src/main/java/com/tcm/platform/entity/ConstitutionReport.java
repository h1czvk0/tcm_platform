package com.tcm.platform.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 体质报告实体类
 */
@Data
@TableName("constitution_report")
public class ConstitutionReport implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 报告ID
     */
    @TableId(value = "report_id", type = IdType.AUTO)
    private Long reportId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 问诊ID
     */
    private Long consultationId;

    /**
     * 体质类型
     */
    private String constitutionType;

    /**
     * 风险提示
     */
    private String riskWarning;

    /**
     * 舌象分析详情
     */
    private String tongueAnalysis;

    /**
     * 饮食建议
     */
    private String dietarySuggestions;

    /**
     * 生活方式建议
     */
    private String lifestyleSuggestions;

    /**
     * 体质特征关键词(JSON格式)
     */
    private String constitutionKeywords;

    /**
     * 生成时间
     */
    private LocalDateTime generateTime;

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
