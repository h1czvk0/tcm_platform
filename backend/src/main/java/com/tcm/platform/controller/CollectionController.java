package com.tcm.platform.controller;

import com.tcm.platform.common.PageResult;
import com.tcm.platform.common.Result;
import com.tcm.platform.service.CollectionService;
import com.tcm.platform.vo.ChineseMedicineVO;
import com.tcm.platform.vo.HerbalDietVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 收藏控制器
 */
@Api(tags = "收藏接口")
@RestController
@RequestMapping("/collection")
public class CollectionController {

    @Autowired
    private CollectionService collectionService;

    @ApiOperation("收藏中药")
    @PostMapping("/medicine/{medicineId}")
    public Result<Void> collectMedicine(@PathVariable Long medicineId,
                                        @RequestParam(required = false) String note,
                                        HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        collectionService.collectMedicine(userId, medicineId, note);
        return Result.success("收藏成功", null);
    }

    @ApiOperation("取消收藏中药")
    @DeleteMapping("/medicine/{medicineId}")
    public Result<Void> uncollectMedicine(@PathVariable Long medicineId,
                                          HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        collectionService.uncollectMedicine(userId, medicineId);
        return Result.success("取消收藏成功", null);
    }

    @ApiOperation("收藏药膳")
    @PostMapping("/diet/{dietId}")
    public Result<Void> collectDiet(@PathVariable Long dietId,
                                    @RequestParam(required = false) String note,
                                    HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        collectionService.collectDiet(userId, dietId, note);
        return Result.success("收藏成功", null);
    }

    @ApiOperation("取消收藏药膳")
    @DeleteMapping("/diet/{dietId}")
    public Result<Void> uncollectDiet(@PathVariable Long dietId,
                                      HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        collectionService.uncollectDiet(userId, dietId);
        return Result.success("取消收藏成功", null);
    }

    @ApiOperation("获取用户收藏的中药列表")
    @GetMapping("/medicine/list")
    public Result<PageResult<ChineseMedicineVO>> getUserMedicineCollections(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        PageResult<ChineseMedicineVO> pageResult = 
            collectionService.getUserMedicineCollections(userId, current, size);
        return Result.success(pageResult);
    }

    @ApiOperation("获取用户收藏的药膳列表")
    @GetMapping("/diet/list")
    public Result<PageResult<HerbalDietVO>> getUserDietCollections(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        PageResult<HerbalDietVO> pageResult = 
            collectionService.getUserDietCollections(userId, current, size);
        return Result.success(pageResult);
    }
}
