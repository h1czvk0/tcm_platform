package com.tcm.platform.service;

import com.tcm.platform.common.PageResult;
import com.tcm.platform.vo.ChineseMedicineVO;
import com.tcm.platform.vo.HerbalDietVO;

/**
 * 收藏服务接口
 */
public interface CollectionService {
    
    /**
     * 收藏中药
     */
    void collectMedicine(Long userId, Long medicineId, String note);
    
    /**
     * 取消收藏中药
     */
    void uncollectMedicine(Long userId, Long medicineId);
    
    /**
     * 收藏药膳
     */
    void collectDiet(Long userId, Long dietId, String note);
    
    /**
     * 取消收藏药膳
     */
    void uncollectDiet(Long userId, Long dietId);
    
    /**
     * 获取用户收藏的中药列表
     */
    PageResult<ChineseMedicineVO> getUserMedicineCollections(Long userId, Long current, Long size);
    
    /**
     * 获取用户收藏的药膳列表
     */
    PageResult<HerbalDietVO> getUserDietCollections(Long userId, Long current, Long size);
}
