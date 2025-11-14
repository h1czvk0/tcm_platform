package com.tcm.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tcm.platform.common.PageResult;
import com.tcm.platform.common.ResultCode;
import com.tcm.platform.entity.*;
import com.tcm.platform.exception.BusinessException;
import com.tcm.platform.mapper.*;
import com.tcm.platform.service.CollectionService;
import com.tcm.platform.vo.ChineseMedicineVO;
import com.tcm.platform.vo.HerbalDietVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 收藏服务实现类
 */
@Service
public class CollectionServiceImpl implements CollectionService {

    @Autowired
    private MedicineCollectionMapper medicineCollectionMapper;

    @Autowired
    private DietCollectionMapper dietCollectionMapper;

    @Autowired
    private ChineseMedicineMapper chineseMedicineMapper;

    @Autowired
    private HerbalDietMapper herbalDietMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void collectMedicine(Long userId, Long medicineId, String note) {
        // 检查中药是否存在
        ChineseMedicine medicine = chineseMedicineMapper.selectById(medicineId);
        if (medicine == null) {
            throw new BusinessException(ResultCode.MEDICINE_NOT_FOUND);
        }

        // 检查是否已收藏
        LambdaQueryWrapper<MedicineCollection> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MedicineCollection::getUserId, userId)
               .eq(MedicineCollection::getMedicineId, medicineId);
        MedicineCollection existing = medicineCollectionMapper.selectOne(wrapper);
        if (existing != null) {
            throw new BusinessException(ResultCode.ALREADY_COLLECTED);
        }

        // 添加收藏
        MedicineCollection collection = new MedicineCollection();
        collection.setUserId(userId);
        collection.setMedicineId(medicineId);
        collection.setNote(note);
        collection.setCollectionTime(LocalDateTime.now());
        medicineCollectionMapper.insert(collection);

        // 增加收藏计数
        chineseMedicineMapper.incrementCollectionCount(medicineId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void uncollectMedicine(Long userId, Long medicineId) {
        LambdaQueryWrapper<MedicineCollection> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MedicineCollection::getUserId, userId)
               .eq(MedicineCollection::getMedicineId, medicineId);
        MedicineCollection collection = medicineCollectionMapper.selectOne(wrapper);
        
        if (collection == null) {
            throw new BusinessException(ResultCode.COLLECTION_NOT_FOUND);
        }

        medicineCollectionMapper.deleteById(collection.getCollectionId());
        
        // 减少收藏计数
        chineseMedicineMapper.decrementCollectionCount(medicineId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void collectDiet(Long userId, Long dietId, String note) {
        // 检查药膳是否存在
        HerbalDiet diet = herbalDietMapper.selectById(dietId);
        if (diet == null) {
            throw new BusinessException(ResultCode.DIET_NOT_FOUND);
        }

        // 检查是否已收藏
        LambdaQueryWrapper<DietCollection> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DietCollection::getUserId, userId)
               .eq(DietCollection::getDietId, dietId);
        DietCollection existing = dietCollectionMapper.selectOne(wrapper);
        if (existing != null) {
            throw new BusinessException(ResultCode.ALREADY_COLLECTED);
        }

        // 添加收藏
        DietCollection collection = new DietCollection();
        collection.setUserId(userId);
        collection.setDietId(dietId);
        collection.setNote(note);
        collection.setCollectionTime(LocalDateTime.now());
        dietCollectionMapper.insert(collection);

        // 增加收藏计数
        herbalDietMapper.incrementCollectionCount(dietId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void uncollectDiet(Long userId, Long dietId) {
        LambdaQueryWrapper<DietCollection> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DietCollection::getUserId, userId)
               .eq(DietCollection::getDietId, dietId);
        DietCollection collection = dietCollectionMapper.selectOne(wrapper);
        
        if (collection == null) {
            throw new BusinessException(ResultCode.COLLECTION_NOT_FOUND);
        }

        dietCollectionMapper.deleteById(collection.getDietCollectionId());
        
        // 减少收藏计数
        herbalDietMapper.decrementCollectionCount(dietId);
    }

    @Override
    public PageResult<ChineseMedicineVO> getUserMedicineCollections(Long userId, Long current, Long size) {
        Page<MedicineCollection> page = new Page<>(current, size);
        LambdaQueryWrapper<MedicineCollection> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MedicineCollection::getUserId, userId)
               .orderByDesc(MedicineCollection::getCollectionTime);
        
        IPage<MedicineCollection> collectionPage = medicineCollectionMapper.selectPage(page, wrapper);

        List<ChineseMedicineVO> voList = collectionPage.getRecords().stream().map(collection -> {
            ChineseMedicine medicine = chineseMedicineMapper.selectById(collection.getMedicineId());
            ChineseMedicineVO vo = new ChineseMedicineVO();
            if (medicine != null) {
                BeanUtils.copyProperties(medicine, vo);
                vo.setIsCollected(true);
            }
            return vo;
        }).filter(vo -> vo.getMedicineId() != null)
          .collect(Collectors.toList());

        return new PageResult<>(collectionPage.getCurrent(), collectionPage.getSize(), 
                                collectionPage.getTotal(), voList);
    }

    @Override
    public PageResult<HerbalDietVO> getUserDietCollections(Long userId, Long current, Long size) {
        Page<DietCollection> page = new Page<>(current, size);
        LambdaQueryWrapper<DietCollection> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DietCollection::getUserId, userId)
               .orderByDesc(DietCollection::getCollectionTime);
        
        IPage<DietCollection> collectionPage = dietCollectionMapper.selectPage(page, wrapper);

        List<HerbalDietVO> voList = collectionPage.getRecords().stream().map(collection -> {
            HerbalDiet diet = herbalDietMapper.selectById(collection.getDietId());
            HerbalDietVO vo = new HerbalDietVO();
            if (diet != null) {
                BeanUtils.copyProperties(diet, vo);
                vo.setIsCollected(true);
            }
            return vo;
        }).filter(vo -> vo.getDietId() != null)
          .collect(Collectors.toList());

        return new PageResult<>(collectionPage.getCurrent(), collectionPage.getSize(), 
                                collectionPage.getTotal(), voList);
    }
}
