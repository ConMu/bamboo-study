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
                .build()
                .globalOperationParameters(globalParameters()); // 添加全局参数
    }

    /**
     * 全局参数配置 - dsNo数据源选择
     */
    private List<Parameter> globalParameters() {
        List<Parameter> parameters = new ArrayList<>();

        // dsNo 数据源参数
        Parameter dsNoParameter = new ParameterBuilder()
                .name("dsNo")
                .description("数据源编号：ds0(数据源1-用户库) 或 ds1(数据源2-人员库)")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .defaultValue("ds0")
                .build();

        parameters.add(dsNoParameter);
        return parameters;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("SpringBoot多数据源API文档 (动态切换版)")
                .description("基于SpringBoot + MyBatis + Druid的多数据源动态切换示例API\n\n" +
                           "📋 **数据源说明**:\n" +
                           "- ds0: 数据源1 (yunxin_recovery数据库，端口3306)\n" +
                           "- ds1: 数据源2 (test数据库，端口4407)\n" +
                           "- 两个数据库都有相同的表结构(users, people)，可任意选择\n\n" +
                           "🔧 **使用方式**:\n" +
                           "- 在请求头中添加 dsNo 参数来指定数据源\n" +
                           "- 不指定时默认使用 ds0 数据源\n" +
                           "- AOP切面会自动根据dsNo切换数据源\n" +
                           "- 所有接口都支持 ds0 和 ds1 两个数据源")
                .contact(new Contact("mucongcong", "https://github.com/mucongcong", "mucongcong@example.com"))
                .version("2.0.0")
                .build();
    }
}