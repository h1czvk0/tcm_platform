package com.tcm.platform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 中医药推广平台主启动类
 * 
 * @author TCM Platform Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableCaching
@EnableTransactionManagement
@MapperScan("com.tcm.platform.mapper")
public class TcmPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(TcmPlatformApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("   中医药推广平台启动成功！");
        System.out.println("   接口文档地址: http://localhost:8080/api/swagger-ui/");
        System.out.println("   健康检查地址: http://localhost:8080/api/health");
        System.out.println("========================================\n");
    }
}
