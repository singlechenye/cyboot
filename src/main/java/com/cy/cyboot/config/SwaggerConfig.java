package com.cy.cyboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        String groupName="3.X版本";
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(new ApiInfoBuilder()
                        .title("这是后端模板 API ")
                        .description("# 这里记录服务端所有的接口的入参，出参等等信息")
                        .termsOfServiceUrl("")
                        .contact(new Contact("cy","http://127.0.0.1:8080/api/doc.html","2089170553@qq.com"))
                        .version("3.0")
                        .build())
                //分组名称
                .groupName(groupName)
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.cy.usercenter.controller"))
                .paths(PathSelectors.any())
                .build();
    }

}