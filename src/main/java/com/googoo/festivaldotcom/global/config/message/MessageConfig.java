package com.googoo.festivaldotcom.global.config.message;

import com.googoo.festivaldotcom.global.utils.MessageUtil;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;

@Configuration
public class MessageConfig {

	@Bean
	public MessageSourceAccessor messageSourceAccessor(MessageSource messageSource) {
		MessageSourceAccessor messageSourceAccessor = new MessageSourceAccessor(messageSource);
		MessageUtil.setMessageSourceAccessor(messageSourceAccessor);
		return messageSourceAccessor;
	}
}