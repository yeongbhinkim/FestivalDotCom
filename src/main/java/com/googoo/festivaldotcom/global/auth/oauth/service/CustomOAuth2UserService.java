package com.googoo.festivaldotcom.global.auth.oauth.service;

import com.googoo.festivaldotcom.function.member.domain.service.UserService;
import com.googoo.festivaldotcom.global.auth.oauth.dto.AuthUserInfo;
import com.googoo.festivaldotcom.global.auth.oauth.dto.CustomOAuth2User;
import com.googoo.festivaldotcom.global.auth.oauth.dto.OAuthUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Lombok 라이브러리의 @Slf4j 어노테이션으로 로그를 사용할 수 있게 해줘.
// @Service 어노테이션으로 이 클래스를 스프링 빈으로 등록해.
// @RequiredArgsConstructor 어노테이션은 final이나 @NonNull이 붙은 필드의 생성자를 자동으로 생성해줘.
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final UserService userService; // 사용자 관련 비즈니스 로직을 처리하는 서비스 클래스

	@Override
	@Transactional // 메소드를 트랜잭션 범위 안에서 실행하도록 설정
	public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
		OAuth2User oauth2User = super.loadUser(oAuth2UserRequest); // OAuth2UserService의 기본 메서드를 호출해 OAuth2User 정보를 로드
		String providerName = oAuth2UserRequest.getClientRegistration().getRegistrationId(); // OAuth2 공급자 이름을 가져와

		// OAuth2User 객체와 공급자 이름을 통해 사용자 정보를 추출
		OAuthUserInfo oauthUserInfo = extractUserInfoFromOAuth2User(oauth2User, providerName);

		// 추출된 사용자 정보를 바탕으로 사용자를 등록하거나 가져옴
		AuthUserInfo user = userService.getOauthId(oauthUserInfo);

		// CustomOAuth2User 객체를 생성해 반환. 이 객체는 OAuth2User 인터페이스를 구현하며,
		// 인증된 사용자의 정보와 권한 등을 포함
		return new CustomOAuth2User(user, oauth2User.getAttributes());
	}

	// OAuth2User 객체와 제공자 이름을 받아 사용자 정보를 추출하는 메서드
	private OAuthUserInfo extractUserInfoFromOAuth2User(OAuth2User oauth2User, String providerName) {
		// OAuthProvider 열거형을 사용해 제공자에 맞는 UserInfo 변환 로직을 수행
		return OAuthProvider
				.getOAuthProviderByName(providerName)
				.toUserInfo(oauth2User);
	}
}

