package com.tcm.platform.controller;

import com.tcm.platform.common.PageResult;
import com.tcm.platform.common.Result;
import com.tcm.platform.entity.HerbalDiet;
import com.tcm.platform.service.HerbalDietService;
import com.tcm.platform.vo.HerbalDietVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 药膳控制器
 */
@Api(tags = "药膳接口")
@RestController
@RequestMapping("/diet")
public class HerbalDietController {

    @Autowired
    private HerbalDietService herbalDietService;

    @ApiOperation("分页查询药膳列表")
    @GetMapping("/page")
    public Result<PageResult<HerbalDietVO>> getDietPage(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String difficulty,
            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        PageResult<HerbalDietVO> pageResult = 
            herbalDietService.getDietPage(current, size, keyword, difficulty, userId);
        return Result.success(pageResult);
    }

    @ApiOperation("获取药膳详情")
    @GetMapping("/{dietId}")
    public Result<HerbalDietVO> getDietDetail(@PathVariable Long dietId,
                                              HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        HerbalDietVO diet = herbalDietService.getDietDetail(dietId, userId);
        return Result.success(diet);
    }

    @ApiOperation("添加药膳（管理员）")
    @PostMapping("/add")
    public Result<Void> addDiet(@RequestBody HerbalDiet diet) {
        herbalDietService.save(diet);
        return Result.success("添加成功", null);
    }

    @ApiOperation("更新药膳（管理员）")
    @PutMapping("/update")
    public Result<Void> updateDiet(@RequestBody HerbalDiet diet) {
        herbalDietService.updateById(diet);
        return Result.success("更新成功", null);
    }

    @ApiOperation("删除药膳（管理员）")
    @DeleteMapping("/{dietId}")
    public Result<Void> deleteDiet(@PathVariable Long dietId) {
        herbalDietService.removeById(dietId);
        return Result.success("删除成功", null);
    }
}
