package com.tcm.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tcm.platform.common.PageResult;
import com.tcm.platform.entity.KnowledgeQuestion;

/**
 * 题库服务接口
 */
public interface QuestionService extends IService<KnowledgeQuestion> {
    
    /**
     * 分页查询题目
     */
    PageResult<KnowledgeQuestion> getQuestionPage(Long current, Long size, 
                                                   String keyword, String category, String difficulty);
}
