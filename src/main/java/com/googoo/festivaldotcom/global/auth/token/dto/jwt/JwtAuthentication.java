package com.googoo.festivaldotcom.global.auth.token.dto.jwt;

import com.googoo.festivaldotcom.global.auth.token.exception.InvalidTokenException;

import java.util.Objects;

public record JwtAuthentication(
        Long id,
        String accessToken

) {
    public JwtAuthentication {
        validateId(id);
        validateToken(accessToken);
    }

    private void validateToken(String accessToken) {
        if (Objects.isNull(accessToken) || accessToken.isBlank()) {
            throw new InvalidTokenException();
        }
    }

    private void validateId(Long userId) {
        if (Objects.isNull(userId) || userId <= 0L) {
            throw new InvalidTokenException();
        }
    }

    @Override
    public String toString() {
        return "JwtAuthentication{" +
                "accessToken='" + accessToken + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

}
