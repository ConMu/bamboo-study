package com.conmu.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Spring Boot 启动类
 *
 * @author mucongcong
 * @date 2025/10/14 15:44
 * @since
 **/
@SpringBootApplication
@EnableAspectJAutoProxy // 启用AspectJ自动代理，支持AOP切面
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
