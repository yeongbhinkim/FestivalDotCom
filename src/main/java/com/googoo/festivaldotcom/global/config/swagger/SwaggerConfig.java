package com.googoo.festivaldotcom.global.config.swagger;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    // public API 그룹을 생성합니다.
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("test") // API 그룹 이름
                .pathsToMatch("/**") // /**/ 으로 시작하는 API 경로를 문서화
                .build();
    }

    // admin API 그룹을 생성합니다.
//    @Bean
//    public GroupedOpenApi adminApi() {
//        return GroupedOpenApi.builder()
//                .group("springshop-admin") // API 그룹 이름
//                .pathsToMatch("/admin/**") // /admin/으로 시작하는 API 경로를 문서화
//                .addOpenApiMethodFilter(method -> method.isAnnotationPresent(Admin.class)) // Admin 어노테이션이 있는 메소드만 포함
//                .build();
//    }
}
