package com.tcm.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tcm.platform.common.PageResult;
import com.tcm.platform.entity.KnowledgeQuestion;
import com.tcm.platform.mapper.KnowledgeQuestionMapper;
import com.tcm.platform.service.QuestionService;
import org.springframework.stereotype.Service;

/**
 * 题库服务实现
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<KnowledgeQuestionMapper, KnowledgeQuestion> 
        implements QuestionService {

    @Override
    public PageResult<KnowledgeQuestion> getQuestionPage(Long current, Long size, 
                                                         String keyword, String category, String difficulty) {
        Page<KnowledgeQuestion> page = new Page<>(current, size);
        LambdaQueryWrapper<KnowledgeQuestion> wrapper = new LambdaQueryWrapper<>();
        
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(KnowledgeQuestion::getQuestionContent, keyword);
        }
        if (category != null && !category.isEmpty()) {
            wrapper.eq(KnowledgeQuestion::getCategory, category);
        }
        if (difficulty != null && !difficulty.isEmpty()) {
            wrapper.eq(KnowledgeQuestion::getDifficulty, difficulty);
        }
        
        IPage<KnowledgeQuestion> questionPage = this.page(page, wrapper);
        return new PageResult<>(questionPage.getCurrent(), questionPage.getSize(), 
                                questionPage.getTotal(), questionPage.getRecords());
    }
}
