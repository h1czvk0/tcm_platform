package com.tcm.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tcm.platform.common.PageResult;
import com.tcm.platform.entity.HerbalDiet;
import com.tcm.platform.vo.HerbalDietVO;

/**
 * 药膳服务接口
 */
public interface HerbalDietService extends IService<HerbalDiet> {
    
    /**
     * 分页查询药膳
     */
    PageResult<HerbalDietVO> getDietPage(Long current, Long size, String keyword, 
                                         String difficulty, Long userId);
    
    /**
     * 获取药膳详情
     */
    HerbalDietVO getDietDetail(Long dietId, Long userId);
    
    /**
     * 增加浏览次数
     */
    void incrementViewsCount(Long dietId);
}
