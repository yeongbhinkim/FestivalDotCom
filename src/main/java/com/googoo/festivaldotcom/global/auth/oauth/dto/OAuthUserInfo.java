package com.googoo.festivaldotcom.global.auth.oauth.dto;

import lombok.Builder;

@Builder
public record OAuthUserInfo(
	String nickname,
	String profileImgUrl,
	String provider,
	String oauthId
) {
}
