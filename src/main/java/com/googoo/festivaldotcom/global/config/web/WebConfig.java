package com.googoo.festivaldotcom.global.config.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AttachProperties attachProperties;

    public WebConfig(AttachProperties attachProperties) {
        this.attachProperties = attachProperties;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String rootDir = attachProperties.getRootDir();
        String handler = attachProperties.getHandler();

        registry.addResourceHandler(handler)
                .addResourceLocations(rootDir);
    }
}
