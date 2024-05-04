package com.googoo.festivaldotcom.global.auth.oauth.handler;


import com.googoo.festivaldotcom.global.auth.oauth.dto.CustomOAuth2User;
import com.googoo.festivaldotcom.global.auth.token.dto.Tokens;
import com.googoo.festivaldotcom.global.auth.token.service.TokenService;
import com.googoo.festivaldotcom.global.utils.CookieUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.googoo.festivaldotcom.global.auth.oauth.repository.HttpCookieOAuthAuthorizationRequestRepository.*;
import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthAuthenticationSuccessHandler
	extends SavedRequestAwareAuthenticationSuccessHandler {

	public static final String QUERY = "restaurantName=";
	private final TokenService tokenService; // 토큰 서비스 의존성

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException { // 스프링 시큐리티 로그인 성공 핸들러 상속
		if (authentication.getPrincipal() instanceof CustomOAuth2User oauth2User) {
			Tokens tokens = tokenService.createTokens(oauth2User.getUserInfo());

			String targetUrl = determineTargetUrl(request, tokens.accessToken());
			setRefreshTokenInCookie(response, tokens.refreshToken(), "refreshToken");  // 리프레시 토큰을 쿠키에 설정

			setAccessTokenInCookie(response, tokens.accessToken(), "accessToken");  // 엑세스 토큰을 쿠키에 설정
			getRedirectStrategy().sendRedirect(request, response, targetUrl);

		} else {
			super.onAuthenticationSuccess(request, response, authentication); // 기본 핸들러 동작 수행
		}
	}

	// 리다이렉트 URL을 결정하는 메소드
	private String determineTargetUrl(HttpServletRequest request, String accessToken) {
		String targetUrl = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
			.map(Cookie::getValue)
			.map(cookie -> URLDecoder.decode(cookie, UTF_8))
			.map(this::encodeKoreanCharacters)
			.orElse(getDefaultTargetUrl());

		return UriComponentsBuilder.fromUriString(targetUrl)
//			.queryParam("accessToken", accessToken) // 액세스 토큰을 쿼리 파라미터로 추가 프론트에 줄때사용
			.build().toUriString();
	}

	// 정규 표현식을 이용해서 한글만 따로 인코딩
	private String encodeKoreanCharacters(String url) {
		Pattern koreanPattern = Pattern.compile("[가-힣]+");
		Matcher matcher = koreanPattern.matcher(url);
		StringBuilder encodedUrl = new StringBuilder();

		while (matcher.find()) {
			String koreanText = matcher.group();
			// 한글 부분만 인코딩
			String encodedKoreanText = URLEncoder.encode(koreanText, StandardCharsets.UTF_8);
			// 인코딩된 한글로 치환
			matcher.appendReplacement(encodedUrl, encodedKoreanText);
		}
		matcher.appendTail(encodedUrl);

		return encodedUrl.toString();
	}

	// 새 리프레시 토큰을 쿠키에 설정하는 메소드
	private void setRefreshTokenInCookie(HttpServletResponse response, String refreshToken, String refreshTokenName ) {
		ResponseCookie token = ResponseCookie.from(refreshTokenName, refreshToken)
			.path(getDefaultTargetUrl())
			.httpOnly(true)
			.sameSite("None")
			.secure(true)
			.maxAge(tokenService.getRefreshTokenExpirySeconds())
			.build();

		response.addHeader("Set-Cookie", token.toString());  // 쿠키 헤더에 추가
	}

	/**
	 * 새로운 리프레시 토큰을 쿠키에 저장하는 메소드입니다.
	 *
	 * @param response         클라이언트에게 보낼 HttpServletResponse 객체입니다.
	 * @param accessToken     클라이언트에게 발급할 엑세스 토큰의 문자열 값입니다.
	 * @param accessTokenName 쿠키에 저장될 엑세스 토큰의 이름입니다.
	 */
	private void setAccessTokenInCookie(HttpServletResponse response, String accessToken, String accessTokenName ) {
		// ResponseCookie 빌더를 사용하여 새로운 쿠키 객체를 생성합니다.
		ResponseCookie token = ResponseCookie.from(accessTokenName, accessToken)  // 쿠키 이름과 값을 설정합니다.
				.path(getDefaultTargetUrl())  // 쿠키의 경로를 설정합니다. 일반적으로 기본 URL이 사용됩니다.
				.httpOnly(true)  // 쿠키에 HttpOnly 속성을 false로 설정하여, JavaScript를 통한 접근을 허용합니다.
				.sameSite("None")  // 쿠키의 SameSite 속성을 'None'으로 설정하여, 모든 요청에 대해 쿠키를 전송하도록 합니다.
				.secure(true)  // Secure 속성을 true로 설정하여, HTTPS를 통해서만 쿠키가 전송되도록 합니다.
				.maxAge(tokenService.getAccessTokenExpirySeconds())  // 쿠키의 최대 수명을 설정합니다. tokenService를 통해 만료 시간을 가져옵니다.
				.build();  // 쿠키 객체를 빌드합니다.

		response.addHeader("Set-Cookie", token.toString());  // 쿠키를 HTTP 응답 헤더에 추가합니다.
	}
}
