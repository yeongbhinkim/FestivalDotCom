package com.googoo.festivaldotcom.global.auth.oauth.dto;

import lombok.Builder;

@Builder
public record OAuthUserInfo(
	String nickName,
	String profileImgUrl,
	String provider,
	String oauthId
) {
}
