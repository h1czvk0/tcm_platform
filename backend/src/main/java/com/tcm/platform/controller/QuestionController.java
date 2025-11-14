package com.tcm.platform.controller;

import com.tcm.platform.common.PageResult;
import com.tcm.platform.common.Result;
import com.tcm.platform.entity.KnowledgeQuestion;
import com.tcm.platform.service.QuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 题库管理控制器
 */
@Api(tags = "题库管理")
@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @ApiOperation("分页查询题目")
    @GetMapping("/page")
    public Result<PageResult<KnowledgeQuestion>> getQuestionPage(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String difficulty) {
        PageResult<KnowledgeQuestion> pageResult = questionService.getQuestionPage(
                current, size, keyword, category, difficulty);
        return Result.success(pageResult);
    }

    @ApiOperation("添加题目")
    @PostMapping("/add")
    public Result<Void> addQuestion(@RequestBody KnowledgeQuestion question) {
        questionService.save(question);
        return Result.success();
    }

    @ApiOperation("更新题目")
    @PutMapping("/update")
    public Result<Void> updateQuestion(@RequestBody KnowledgeQuestion question) {
        questionService.updateById(question);
        return Result.success();
    }

    @ApiOperation("删除题目")
    @DeleteMapping("/{questionId}")
    public Result<Void> deleteQuestion(@PathVariable Long questionId) {
        questionService.removeById(questionId);
        return Result.success();
    }
}
