package com.conmu.sms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mucongcong
 * @date 2025/10/14 17:50
 * @since
 **/
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.conmu.sms.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("SpringBoot多数据源API文档 (Mapper级热切换版)")
                .description("基于Mapper维度的数据源热切换系统\n\n" +
                           "🚀 **核心特性**:\n" +
                           "- Mapper级别数据源绑定 (PeopleMapper->ds0, UserMapper->ds1)\n" +
                           "- 支持运行时热切换数据源配置\n" +
                           "- 无需重启应用即可修改数据源策略\n\n" +
                           "📋 **数据源配置**:\n" +
                           "- ds0: yunxin_recovery数据库 (端口3306)\n" +
                           "- ds1: test数据库 (端口4407)\n\n" +
                           "🔧 **使用方式**:\n" +
                           "1. 业务接口: 自动根据Mapper配置选择数据源\n" +
                           "2. 热切换接口: /api/datasource/** 管理数据源配置\n" +
                           "3. 实时生效: 配置修改后立即生效，无需重启")
                .contact(new Contact("mucongcong", "https://github.com/mucongcong", "mucongcong@example.com"))
                .version("3.0.0")
                .build();
    }
}