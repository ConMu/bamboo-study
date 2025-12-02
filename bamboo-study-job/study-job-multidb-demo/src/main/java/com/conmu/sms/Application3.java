package com.conmu.sms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Spring Boot 启动类
 *
 * @author mucongcong
 * @date 2025/10/14 15:44
 * @since
 **/

@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class
})
@EnableAspectJAutoProxy // 启用AspectJ自动代理，支持AOP切面
@MapperScan(basePackages = "com.conmu.sms.dao.mapper") // 扫描Mapper接口
public class Application3 {
    public static void main(String[] args) {
        SpringApplication.run(Application3.class, args);
    }
}
