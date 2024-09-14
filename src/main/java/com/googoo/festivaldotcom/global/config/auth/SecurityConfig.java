package com.googoo.festivaldotcom.global.config.auth;

import com.googoo.festivaldotcom.global.auth.oauth.handler.OAuthAuthenticationFailureHandler;
import com.googoo.festivaldotcom.global.auth.oauth.handler.OAuthAuthenticationSuccessHandler;
import com.googoo.festivaldotcom.global.auth.oauth.repository.HttpCookieOAuthAuthorizationRequestRepository;
import com.googoo.festivaldotcom.global.auth.oauth.service.CustomOAuth2UserService;
import com.googoo.festivaldotcom.global.auth.token.filter.JwtAuthenticationEntryPoint;
import com.googoo.festivaldotcom.global.auth.token.filter.JwtAuthenticationFilter;
import com.googoo.festivaldotcom.global.base.exception.ExceptionHandlerFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final OAuthAuthenticationSuccessHandler oauthAuthenticationSuccessHandler;
	private final OAuthAuthenticationFailureHandler oauthAuthenticationFailureHandler;
	private final HttpCookieOAuthAuthorizationRequestRepository httpCookieOAuthAuthorizationRequestRepository;
	private final ExceptionHandlerFilter exceptionHandlerFilter;
	private final CustomOAuth2UserService oAuth2UserService;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				// 요청에 대한 보안을 구성합니다.
				.authorizeHttpRequests(authz -> authz
						.requestMatchers(HttpMethod.OPTIONS, "/api/*").permitAll()
//						.requestMatchers(HttpMethod.OPTIONS, "/ws").permitAll()
//						.requestMatchers(HttpMethod.OPTIONS, "/ws/*").permitAll()
						.requestMatchers("/docs/**", "/oauth2/**", "/favicon.ico").permitAll()
						.requestMatchers("/api/v1/tokens").permitAll()
						.requestMatchers("/img/**").permitAll()
						.requestMatchers("/js/**").permitAll()
						.requestMatchers("/css/**").permitAll()
						.requestMatchers("/api-docs/**").permitAll()
						.requestMatchers("/oauthlogin").permitAll()
						.requestMatchers("/").permitAll()
						.requestMatchers("/chat/**").permitAll()  //추후 삭제해야함 테스트용으로 넣어둠

						.requestMatchers("/api/v1/festival/**").permitAll()
						.requestMatchers("/festivalImg/**").permitAll()

						.requestMatchers("/actuator/prometheus").permitAll()
						.anyRequest().authenticated()
				)


				// 기본 HTTP 인증을 비활성화합니다.
				.httpBasic(httpBasic -> httpBasic.disable())

				// 기억하기 기능을 비활성화합니다.
				.rememberMe(rememberMe -> rememberMe.disable())

				// CSRF 보호를 비활성화합니다.
				.csrf(csrf -> csrf.disable())

				// 로그아웃을 비활성화합니다.
				.logout(logout -> logout.disable())

				// 요청 캐시를 비활성화합니다.
				.requestCache(requestCache -> requestCache.disable())

				// 폼 로그인을 비활성화합니다.
				.formLogin(formLogin -> formLogin.disable())

				// 헤더 보안을 비활성화합니다.
				.headers(headers -> headers.disable())

				// 세션 관리를 구성합니다.
				.sessionManagement(sessionManagement ->
						sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				)

				// OAuth 2 로그인을 구성합니다.
				.oauth2Login(oauth2 -> oauth2
						.authorizationEndpoint(authz -> authz
								.baseUri("/oauth2/authorization")
								.authorizationRequestRepository(httpCookieOAuthAuthorizationRequestRepository)
						)
						.redirectionEndpoint(redirectionEndpointConfig -> redirectionEndpointConfig
								.baseUri("/test")
						)
						.successHandler(oauthAuthenticationSuccessHandler)
						.failureHandler(oauthAuthenticationFailureHandler)
				)

				// 예외 처리를 구성합니다.
				.exceptionHandling(exception -> exception
						.authenticationEntryPoint(jwtAuthenticationEntryPoint)
				)

				// 필터를 추가합니다.
				.addFilterBefore(jwtAuthenticationFilter, OAuth2AuthorizationRequestRedirectFilter.class)
				.addFilterBefore(exceptionHandlerFilter, JwtAuthenticationFilter.class);

		// 설정을 빌드합니다.
		return http.build();
	}

}

