package com.conmu.sms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger配置类
 * @author mucongcong  
 * @date 2025/10/14 18:18
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
                // 扫描指定包下的controller
                .apis(RequestHandlerSelectors.basePackage("com.conmu.sms.controller"))
                // 扫描所有路径
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 构建API文档信息
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("SpringBoot+MyBatis+Druid多数据源API文档")
                .description("演示SpringBoot集成MyBatis多数据源的REST API")
                .contact(new Contact("mucongcong", "https://github.com/mucongcong", "mucongcong@example.com"))
                .version("1.0.0")
                .build();
    }
}