package com.tcm.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tcm.platform.common.PageResult;
import com.tcm.platform.entity.ChineseMedicine;
import com.tcm.platform.vo.ChineseMedicineVO;

/**
 * 中药材服务接口
 */
public interface ChineseMedicineService extends IService<ChineseMedicine> {
    
    /**
     * 分页查询中药材
     */
    PageResult<ChineseMedicineVO> getMedicinePage(Long current, Long size, String keyword, 
                                                   String nature, Long userId);
    
    /**
     * 获取中药材详情
     */
    ChineseMedicineVO getMedicineDetail(Long medicineId, Long userId);
    
    /**
     * 增加浏览次数
     */
    void incrementViewsCount(Long medicineId);
}
