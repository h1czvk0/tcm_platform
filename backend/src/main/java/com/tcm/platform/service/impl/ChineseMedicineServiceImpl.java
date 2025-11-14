package com.tcm.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tcm.platform.common.PageResult;
import com.tcm.platform.common.ResultCode;
import com.tcm.platform.entity.ChineseMedicine;
import com.tcm.platform.entity.MedicineCollection;
import com.tcm.platform.exception.BusinessException;
import com.tcm.platform.mapper.ChineseMedicineMapper;
import com.tcm.platform.mapper.MedicineCollectionMapper;
import com.tcm.platform.service.ChineseMedicineService;
import com.tcm.platform.utils.RedisUtil;
import com.tcm.platform.vo.ChineseMedicineVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 中药材服务实现类
 */
@Service
public class ChineseMedicineServiceImpl extends ServiceImpl<ChineseMedicineMapper, ChineseMedicine> 
        implements ChineseMedicineService {

    @Autowired
    private ChineseMedicineMapper chineseMedicineMapper;

    @Autowired
    private MedicineCollectionMapper medicineCollectionMapper;

    @Autowired
    private RedisUtil redisUtil;

    private static final String MEDICINE_CACHE_KEY = "medicine:detail:";

    @Override
    public PageResult<ChineseMedicineVO> getMedicinePage(Long current, Long size, String keyword, 
                                                         String nature, Long userId) {
        Page<ChineseMedicine> page = new Page<>(current, size);
        IPage<ChineseMedicine> medicinePage = chineseMedicineMapper.selectMedicinePage(page, keyword, nature);

        List<ChineseMedicineVO> voList = medicinePage.getRecords().stream().map(medicine -> {
            ChineseMedicineVO vo = new ChineseMedicineVO();
            BeanUtils.copyProperties(medicine, vo);
            
            // 检查是否已收藏
            if (userId != null) {
                LambdaQueryWrapper<MedicineCollection> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(MedicineCollection::getUserId, userId)
                       .eq(MedicineCollection::getMedicineId, medicine.getMedicineId());
                long count = medicineCollectionMapper.selectCount(wrapper);
                vo.setIsCollected(count > 0);
            } else {
                vo.setIsCollected(false);
            }
            
            return vo;
        }).collect(Collectors.toList());

        return new PageResult<>(medicinePage.getCurrent(), medicinePage.getSize(), 
                                medicinePage.getTotal(), voList);
    }

    @Override
    public ChineseMedicineVO getMedicineDetail(Long medicineId, Long userId) {
        // 先从缓存获取
        String cacheKey = MEDICINE_CACHE_KEY + medicineId;
        Object cached = redisUtil.get(cacheKey);
        
        ChineseMedicine medicine;
        if (cached != null) {
            medicine = (ChineseMedicine) cached;
        } else {
            medicine = chineseMedicineMapper.selectById(medicineId);
            if (medicine == null) {
                throw new BusinessException(ResultCode.MEDICINE_NOT_FOUND);
            }
            // 缓存30分钟
            redisUtil.set(cacheKey, medicine, 30, TimeUnit.MINUTES);
        }

        // 增加浏览次数
        incrementViewsCount(medicineId);

        // 转换为VO
        ChineseMedicineVO vo = new ChineseMedicineVO();
        BeanUtils.copyProperties(medicine, vo);

        // 检查是否已收藏
        if (userId != null) {
            LambdaQueryWrapper<MedicineCollection> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(MedicineCollection::getUserId, userId)
                   .eq(MedicineCollection::getMedicineId, medicineId);
            long count = medicineCollectionMapper.selectCount(wrapper);
            vo.setIsCollected(count > 0);
        } else {
            vo.setIsCollected(false);
        }

        return vo;
    }

    @Override
    public void incrementViewsCount(Long medicineId) {
        chineseMedicineMapper.incrementViewsCount(medicineId);
        // 删除缓存
        redisUtil.delete(MEDICINE_CACHE_KEY + medicineId);
    }
}
