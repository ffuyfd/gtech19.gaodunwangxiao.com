package com.example.gtech19.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Swagger配置类
 * 配置API文档生成相关设置
 */
@Configuration
@EnableOpenApi
public class SwaggerConfig {

    /**
     * 创建Docket Bean，配置Swagger基本信息
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                // 设置扫描的包路径
                .apis(RequestHandlerSelectors.basePackage("com.example.gtech19.controller"))
                // 配置路径选择器，匹配所有路径
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 配置API的基本信息
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 设置标题
                .title("gtech19项目API文档")
                // 设置描述
                .description("gtech19项目的接口文档，包括用户管理和大模型调用等功能")
                // 设置版本
                .version("1.0.0")
                // 构建API信息
                .build();
    }
}