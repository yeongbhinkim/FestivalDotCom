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
                .group("public") // API 그룹 이름
                .pathsToMatch("/api/**") // /api/로 시작하는 API 경로만 문서화
                .build();
    }
}
