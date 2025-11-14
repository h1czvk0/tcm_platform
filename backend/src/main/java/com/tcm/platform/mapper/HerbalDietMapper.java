package com.tcm.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tcm.platform.entity.HerbalDiet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 药膳Mapper接口
 */
@Mapper
public interface HerbalDietMapper extends BaseMapper<HerbalDiet> {
    
    /**
     * 分页查询药膳
     */
    IPage<HerbalDiet> selectDietPage(Page<HerbalDiet> page, 
                                      @Param("keyword") String keyword,
                                      @Param("difficulty") String difficulty);
    
    /**
     * 增加浏览次数
     */
    void incrementViewsCount(@Param("dietId") Long dietId);
    
    /**
     * 增加收藏次数
     */
    void incrementCollectionCount(@Param("dietId") Long dietId);
    
    /**
     * 减少收藏次数
     */
    void decrementCollectionCount(@Param("dietId") Long dietId);
}
