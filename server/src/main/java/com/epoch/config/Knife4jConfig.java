package com.epoch.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4jConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Project-Api")
                        .version("1.0")
                        .contact(new Contact().name("Epoch"))
                        .description("信息工程学院竞赛管理系统项目的接口文档"));
    }
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public-apis")
                .packagesToScan("com.epoch.dao", "com.epoch.vo","com.epoch.entity") // 必须包含VO包
                .build();
    }

}