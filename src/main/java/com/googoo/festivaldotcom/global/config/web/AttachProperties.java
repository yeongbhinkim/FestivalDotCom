package com.googoo.festivaldotcom.global.config.web;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "attach")
public class AttachProperties {
    private String rootDir;
    private String handler;

}
