package com.tcm.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tcm.platform.entity.Administrator;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 管理员Mapper接口
 */
@Mapper
public interface AdministratorMapper extends BaseMapper<Administrator> {
    
    /**
     * 根据管理员名查询
     */
    Administrator selectByAdminName(@Param("adminName") String adminName);
}
