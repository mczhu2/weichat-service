package com.weichat.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * Swagger API文档配置
 * 使用knife4j增强版，提供更好的文档体验
 *
 * @author weichat
 */
@Configuration
@EnableSwagger2WebMvc
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.weichat.api.controller"))
                .paths(PathSelectors.any())
                .build()
                .groupName("企微服务API");
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("企微服务 API 文档")
                .description("企微服务接口文档，提供企微号管理、群管理、消息、标签、朋友圈等功能接口")
                .version("1.0.0")
                .contact(new Contact("WeChat Team", "", ""))
                .build();
    }
}
