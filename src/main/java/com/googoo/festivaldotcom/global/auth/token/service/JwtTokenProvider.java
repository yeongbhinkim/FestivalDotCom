package com.googoo.festivaldotcom.global.auth.token.service;

import com.googoo.festivaldotcom.global.auth.token.exception.ExpiredTokenException;
import com.googoo.festivaldotcom.global.auth.token.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component // 스프링에서 이 클래스를 빈으로 관리하도록 설정
public class JwtTokenProvider {

    private static final long MILLI_SECOND = 1000L; // 밀리초를 초로 변환하기 위한 상수

    private final String issuer; // 토큰 발행자
    private final String secretKey; // 토큰을 암호화할 때 사용할 비밀 키
    private final long accessTokenExpirySeconds; // 액세스 토큰의 유효 시간(초)

    // 생성자를 통해 환경 변수에서 토큰 정보를 주입받음
    public JwtTokenProvider(
            @Value("${jwt.issuer}") String issuer,
            @Value("${jwt.secret-key}") String secretKey,
            @Value("${jwt.expiry-seconds.access-token}") long accessTokenExpirySeconds
    ) {
        this.issuer = issuer;
        this.secretKey = secretKey;
        this.accessTokenExpirySeconds = accessTokenExpirySeconds;
    }

    // 사용자 ID와 역할 정보를 받아 액세스 토큰 생성
    public String createAccessToken(Long userId, String role) {
        Map<String, Object> claims = Map.of("userId", userId, "role", role);
        return this.createAccessToken(claims);
    }

    // 클레임을 기반으로 JWT 액세스 토큰을 생성하는 메서드
    public String createAccessToken(Map<String, Object> claims) {
        Date now = new Date(); // 현재 시간
        Date expiredDate = new Date(now.getTime() + accessTokenExpirySeconds * MILLI_SECOND); // 만료 시간 설정

        return Jwts.builder()
                .setIssuer(issuer) // 토큰 발행자 설정
                .setClaims(claims) // 클레임 추가
                .setIssuedAt(now) // 토큰 발행 시간 설정
                .setExpiration(expiredDate) // 토큰 만료 시간 설정
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8))) // 비밀 키로 서명
                .compact(); // 토큰 생성
    }

    // 토큰에서 클레임(사용자 정보 및 권한 등)을 추출
    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 토큰의 유효성을 검증하는 메서드
    public void validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException(); // 토큰이 만료된 경우 예외 발생
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException(); // 토큰이 유효하지 않은 경우 예외 발생
        }
    }

    private Set<String> blacklistedTokens = new HashSet<>();

    public void invalidateToken(String token) {
        blacklistedTokens.add(token);
    }

    public boolean isTokenValid(String token) {
        return !blacklistedTokens.contains(token);
    }

}
