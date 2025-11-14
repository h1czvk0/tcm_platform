package com.tcm.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tcm.platform.common.PageResult;
import com.tcm.platform.common.ResultCode;
import com.tcm.platform.entity.DietCollection;
import com.tcm.platform.entity.HerbalDiet;
import com.tcm.platform.exception.BusinessException;
import com.tcm.platform.mapper.DietCollectionMapper;
import com.tcm.platform.mapper.HerbalDietMapper;
import com.tcm.platform.service.HerbalDietService;
import com.tcm.platform.utils.RedisUtil;
import com.tcm.platform.vo.HerbalDietVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 药膳服务实现类
 */
@Service
public class HerbalDietServiceImpl extends ServiceImpl<HerbalDietMapper, HerbalDiet> 
        implements HerbalDietService {

    @Autowired
    private HerbalDietMapper herbalDietMapper;

    @Autowired
    private DietCollectionMapper dietCollectionMapper;

    @Autowired
    private RedisUtil redisUtil;

    private static final String DIET_CACHE_KEY = "diet:detail:";

    @Override
    public PageResult<HerbalDietVO> getDietPage(Long current, Long size, String keyword, 
                                                String difficulty, Long userId) {
        Page<HerbalDiet> page = new Page<>(current, size);
        IPage<HerbalDiet> dietPage = herbalDietMapper.selectDietPage(page, keyword, difficulty);

        List<HerbalDietVO> voList = dietPage.getRecords().stream().map(diet -> {
            HerbalDietVO vo = new HerbalDietVO();
            BeanUtils.copyProperties(diet, vo);
            
            // 检查是否已收藏
            if (userId != null) {
                LambdaQueryWrapper<DietCollection> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(DietCollection::getUserId, userId)
                       .eq(DietCollection::getDietId, diet.getDietId());
                long count = dietCollectionMapper.selectCount(wrapper);
                vo.setIsCollected(count > 0);
            } else {
                vo.setIsCollected(false);
            }
            
            return vo;
        }).collect(Collectors.toList());

        return new PageResult<>(dietPage.getCurrent(), dietPage.getSize(), 
                                dietPage.getTotal(), voList);
    }

    @Override
    public HerbalDietVO getDietDetail(Long dietId, Long userId) {
        // 先从缓存获取
        String cacheKey = DIET_CACHE_KEY + dietId;
        Object cached = redisUtil.get(cacheKey);
        
        HerbalDiet diet;
        if (cached != null) {
            diet = (HerbalDiet) cached;
        } else {
            diet = herbalDietMapper.selectById(dietId);
            if (diet == null) {
                throw new BusinessException(ResultCode.DIET_NOT_FOUND);
            }
            // 缓存30分钟
            redisUtil.set(cacheKey, diet, 30, TimeUnit.MINUTES);
        }

        // 增加浏览次数
        incrementViewsCount(dietId);

        // 转换为VO
        HerbalDietVO vo = new HerbalDietVO();
        BeanUtils.copyProperties(diet, vo);

        // 检查是否已收藏
        if (userId != null) {
            LambdaQueryWrapper<DietCollection> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(DietCollection::getUserId, userId)
                   .eq(DietCollection::getDietId, dietId);
            long count = dietCollectionMapper.selectCount(wrapper);
            vo.setIsCollected(count > 0);
        } else {
            vo.setIsCollected(false);
        }

        return vo;
    }

    @Override
    public void incrementViewsCount(Long dietId) {
        herbalDietMapper.incrementViewsCount(dietId);
        // 删除缓存
        redisUtil.delete(DIET_CACHE_KEY + dietId);
    }
}
