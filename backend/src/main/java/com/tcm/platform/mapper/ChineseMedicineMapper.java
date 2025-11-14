package com.tcm.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tcm.platform.entity.ChineseMedicine;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 中药材Mapper接口
 */
@Mapper
public interface ChineseMedicineMapper extends BaseMapper<ChineseMedicine> {
    
    /**
     * 分页查询中药材
     */
    IPage<ChineseMedicine> selectMedicinePage(Page<ChineseMedicine> page, 
                                               @Param("keyword") String keyword,
                                               @Param("nature") String nature);
    
    /**
     * 增加浏览次数
     */
    void incrementViewsCount(@Param("medicineId") Long medicineId);
    
    /**
     * 增加收藏次数
     */
    void incrementCollectionCount(@Param("medicineId") Long medicineId);
    
    /**
     * 减少收藏次数
     */
    void decrementCollectionCount(@Param("medicineId") Long medicineId);
}
