package com.googoo.festivaldotcom.global.auth.oauth.dto;

public record AuthUserInfo(
	Long id,
	String nickname,
	String role
) {
}
