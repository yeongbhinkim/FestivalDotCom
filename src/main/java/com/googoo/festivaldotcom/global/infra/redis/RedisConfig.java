package com.googoo.festivaldotcom.global.infra.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration  // 스프링 설정 클래스임을 선언
@RequiredArgsConstructor  // Lombok을 사용하여 필요한 생성자를 자동으로 생성
public class RedisConfig {

	@Value("${spring.config.redis.host}")  // application.properties에서 Redis 호스트 주소를 주입
	private String redisHost;

	@Value("${spring.config.redis.port}")  // application.properties에서 Redis 포트를 주입
	private int redisPort;

	@Bean  // 스프링 빈으로 등록
	public RedisConnectionFactory redisConnectionFactory() {
		// Lettuce를 사용한 RedisConnection 생성
		return new LettuceConnectionFactory(redisHost, redisPort);
	}

	@Bean  // 스프링 빈으로 등록
	public StringRedisTemplate stringRedisTemplate() {
		StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
		stringRedisTemplate.setConnectionFactory(redisConnectionFactory());
		stringRedisTemplate.setDefaultSerializer(new StringRedisSerializer()); // Redis-cli을 통해 데이터를 확인할 때 편리하도록 문자열 직렬화 사용
		return stringRedisTemplate;
	}

	@Bean  // 스프링 빈으로 등록
	public RedisCacheManager redisCacheManager() {
		// 기본 캐시 구성 설정
		RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration
				.defaultCacheConfig()
				.disableCachingNullValues()  // null 값 캐싱 비활성화
				.serializeKeysWith(
						RedisSerializationContext.SerializationPair
								.fromSerializer(new StringRedisSerializer()))  // 키 직렬화 방식 설정
				.serializeValuesWith(
						RedisSerializationContext.SerializationPair
								.fromSerializer(new GenericJackson2JsonRedisSerializer()));  // 값 직렬화 방식 설정

		Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();

		// "User" 캐시 이름으로 별도의 TTL(Time To Live) 설정
		redisCacheConfigurationMap.put("User",
				redisCacheConfiguration.entryTtl(Duration.ofMinutes(10)));

		// 캐시 매니저 구성
		return RedisCacheManager.RedisCacheManagerBuilder
				.fromConnectionFactory(redisConnectionFactory())
				.cacheDefaults(redisCacheConfiguration)  // 기본 캐시 구성 사용
				.withInitialCacheConfigurations(redisCacheConfigurationMap)  // 초기 캐시 구성 추가
				.build();
	}
}
