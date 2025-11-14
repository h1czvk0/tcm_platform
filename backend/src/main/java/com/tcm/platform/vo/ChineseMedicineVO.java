package com.tcm.platform.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 中药材VO
 */
@Data
public class ChineseMedicineVO {
    
    private Long medicineId;
    private String medicineName;
    private String medicineAlias;
    private String nature;
    private String flavor;
    private String meridians;
    private String functions;
    private String usage;
    private String contraindications;
    private String imageUrl;
    private Integer viewsCount;
    private Integer collectionCount;
    private Boolean isCollected;  // 是否已收藏
    private LocalDateTime createdAt;
}
