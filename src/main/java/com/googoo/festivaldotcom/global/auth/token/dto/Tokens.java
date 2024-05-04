package com.googoo.festivaldotcom.global.auth.token.dto;

public record Tokens(
        String accessToken,
        String refreshToken
) {
}
