package com.conmu.sms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author mucongcong
 * @date 2024/06/19 19:39
 * @since
 **/
@SpringBootApplication
@MapperScan(basePackages = {"com.conmu.sms.dao"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}