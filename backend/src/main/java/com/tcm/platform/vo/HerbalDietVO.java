package com.tcm.platform.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 药膳VO
 */
@Data
public class HerbalDietVO {
    
    private Long dietId;
    private String dietName;
    private String description;
    private String efficacy;
    private String ingredients;
    private String preparationSteps;
    private Integer cookingTime;
    private String difficultyLevel;
    private String season;
    private String imageUrl;
    private Integer viewsCount;
    private Integer collectionCount;
    private Boolean isCollected;  // 是否已收藏
    private LocalDateTime createdAt;
}
