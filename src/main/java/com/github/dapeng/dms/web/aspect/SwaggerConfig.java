package com.github.dapeng.dms.web.aspect;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket doDocket() {

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                //指定url匹配路径
                .paths(PathSelectors.any())
                //指定扫描哪些包下api接口
                //             .apis(RequestHandlerSelectors.basePackage("com.yunji.popmd.admin.controller"))
                //只扫描写了注解的类和方法
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .build()
                //进行分组(注意不要和其他Docket Bean 同名,否则会导致无法启动)
                .groupName("mockserver")
                .apiInfo(apiInfo())
                .enable(true)    //现网关闭swagger
                ;
    }

    /**
     * apiInfo
     * 定义基本文档信息
     *
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Dapeng MockServer API文档")
                .description("")
                .license("License Version 1.0")
                .version("1.0").build();
    }
}