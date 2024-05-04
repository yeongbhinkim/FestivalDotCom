package com.googoo.festivaldotcom.global.auth.token.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

@Getter // 클래스 필드의 getter 메소드를 자동 생성
@NoArgsConstructor(access = PROTECTED) // 기본 생성자를 생성, 외부에서의 직접 생성을 방지하기 위해 protected 접근 제어자 사용
@RedisHash(value = "refreshToken") // 이 객체를 Redis에서 'refreshToken' 해시로 저장하기 위한 설정
public class RefreshToken {

    @Id
    private String refreshToken; // Redis 키로 사용될 리프레시 토큰 값

    private Long userId; // 토큰과 연결된 유저의 ID

    private String role; // 유저의 권한을 나타내는 필드

    @TimeToLive
    private long expiration; // 토큰의 만료 시간(초 단위), Redis에서 자동으로 만료 관리

    @Builder
    public RefreshToken(String refreshToken, Long userId, String role, long expiration) {
        this.refreshToken = checkRefreshToken(refreshToken);
        this.userId = checkUserId(userId);
        this.role = checkRole(role);
        this.expiration = checkExpiration(expiration);
    }

    private String checkRefreshToken(String refreshToken) {
        // 리프레시 토큰의 유효성을 검사하는 메소드
        if (!Objects.nonNull(refreshToken) || refreshToken.isBlank()) {
            throw new IllegalArgumentException("올바르지 않은 토큰 값");
        }
        return refreshToken;
    }

    private Long checkUserId(Long userId) {
        // 유저 ID의 유효성을 검사하는 메소드
        if (!Objects.nonNull(userId) || userId < 1) {
            throw new IllegalArgumentException("올바르지 않은 유저 아이디");
        }
        return userId;
    }

    private String checkRole(String role) {
        // 유저 권한의 유효성을 검사하는 메소드
        if (!Objects.nonNull(role) || role.isBlank()) {
            throw new IllegalArgumentException("올바르지 않은 권한");
        }
        return role;
    }

    private long checkExpiration(long expiration) {
        // 토큰 만료 시간의 유효성을 검사하는 메소드
        if (expiration < 1) {
            throw new IllegalArgumentException("올바르지 않은 만료 시간");
        }
        return expiration;
    }

}
