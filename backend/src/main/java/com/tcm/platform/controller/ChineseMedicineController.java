package com.tcm.platform.controller;

import com.tcm.platform.common.PageResult;
import com.tcm.platform.common.Result;
import com.tcm.platform.entity.ChineseMedicine;
import com.tcm.platform.service.ChineseMedicineService;
import com.tcm.platform.vo.ChineseMedicineVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 中药材控制器
 */
@Api(tags = "中药材接口")
@RestController
@RequestMapping("/medicine")
public class ChineseMedicineController {

    @Autowired
    private ChineseMedicineService chineseMedicineService;

@ApiOperation("分页查询中药")
@GetMapping("/page")
public Result<PageResult<ChineseMedicineVO>> getMedicinePage(
        @RequestParam(defaultValue = "1") Long current,
        @RequestParam(defaultValue = "10") Long size,
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) String nature,
        @RequestParam(required = false) Long userId) {
    
    PageResult<ChineseMedicineVO> pageResult = chineseMedicineService.getMedicinePage(
            current, size, keyword, nature, userId);
    return Result.success(pageResult);
}


    @ApiOperation("获取中药材详情")
    @GetMapping("/{medicineId}")
    public Result<ChineseMedicineVO> getMedicineDetail(@PathVariable Long medicineId,
                                                       HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        ChineseMedicineVO medicine = chineseMedicineService.getMedicineDetail(medicineId, userId);
        return Result.success(medicine);
    }

    @ApiOperation("添加中药材（管理员）")
    @PostMapping("/add")
    public Result<Void> addMedicine(@RequestBody ChineseMedicine medicine) {
        chineseMedicineService.save(medicine);
        return Result.success("添加成功", null);
    }

    @ApiOperation("更新中药材（管理员）")
    @PutMapping("/update")
    public Result<Void> updateMedicine(@RequestBody ChineseMedicine medicine) {
        chineseMedicineService.updateById(medicine);
        return Result.success("更新成功", null);
    }

    @ApiOperation("删除中药材（管理员）")
    @DeleteMapping("/{medicineId}")
    public Result<Void> deleteMedicine(@PathVariable Long medicineId) {
        chineseMedicineService.removeById(medicineId);
        return Result.success("删除成功", null);
    }
}
