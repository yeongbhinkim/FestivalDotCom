package com.googoo.festivaldotcom.global.auth.token.service;

import com.googoo.festivaldotcom.global.auth.oauth.dto.AuthUserInfo;
import com.googoo.festivaldotcom.global.auth.token.dto.Tokens;
import com.googoo.festivaldotcom.global.auth.token.dto.jwt.JwtAuthentication;
import com.googoo.festivaldotcom.global.auth.token.dto.jwt.JwtAuthenticationToken;
import com.googoo.festivaldotcom.global.auth.token.exception.NotFoundCookieException;
import com.googoo.festivaldotcom.global.auth.token.exception.RefreshTokenNotFoundException;
import com.googoo.festivaldotcom.global.auth.token.model.RefreshToken;
import com.googoo.festivaldotcom.global.auth.token.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service // 스프링 컨테이너에서 관리하는 서비스 빈으로 선언
@RequiredArgsConstructor // 롬복을 이용하여 필수 매개변수(최종 필드)를 받는 생성자를 자동 생성
@Transactional(readOnly = true) // 클래스 수준에서 읽기 전용 트랜잭션 설정
public class TokenService { // TODO: authService와 TokenService로 분리하는 것이 좋을지 고민해보기

    private final JwtTokenProvider jwtTokenProvider; // JWT 토큰 관리 제공자
    private final RefreshTokenRepository refreshTokenRepository; // 리프레시 토큰 저장소

    private final OAuth2AuthorizedClientService authorizedClientService;

    @Value("${jwt.expiry-seconds.refresh-token:36000}") // 리프레시 토큰의 만료 시간을 환경 변수에서 가져오거나 기본값 설정
    private int refreshTokenExpirySeconds;

    @Value("${jwt.expiry-seconds.access-token:1800}") // 리프레시 토큰의 만료 시간을 환경 변수에서 가져오거나 기본값 설정
    private int accessTokenExpirySeconds;

    public Tokens createTokens(AuthUserInfo userInfo) { // 사용자 정보를 받아 액세스 토큰과 리프레시 토큰을 생성
        Long userId = userInfo.id();
        String userRole = userInfo.role();

        String accessToken = createAccessToken(userId, userRole);
        String refreshToken = createRefreshToken(userId, userRole);

        return new Tokens(accessToken, refreshToken);
    }

    @Transactional // 쓰기 트랜잭션을 위해 메소드 수준에서 설정 변경
    public String createRefreshToken(Long userId, String userRole) { // 리프레시 토큰 생성
        RefreshToken refreshToken = new RefreshToken(UUID.randomUUID().toString(), userId, userRole, getRefreshTokenExpirySeconds());
        return refreshTokenRepository.save(refreshToken).getRefreshToken();
    }

    @Transactional // 액세스 토큰 재발급을 위한 리프레시 토큰 검증
    public String getAccessTokensByRefreshToken(@NotBlank String refreshToken) {
        checkRefreshToken(refreshToken); // 리프레시 토큰 유효성 검증

        System.out.println("refreshToken = " + refreshToken);
        return refreshTokenRepository.findById(refreshToken)
                .map(token -> createAccessToken(token.getUserId(), token.getRole())) // 유효한 경우 새 액세스 토큰 생성
                .orElseThrow(RefreshTokenNotFoundException::new); // 리프레시 토큰이 없는 경우 예외 발생
    }

    @Transactional // 리프레시 토큰 삭제
    public void deleteRefreshToken(String refreshToken) {

        checkRefreshToken(refreshToken); // 유효성 검증
        refreshTokenRepository.findById(refreshToken)
                .ifPresent(refreshTokenRepository::delete); // 리프레시 토큰이 존재하는 경우 삭제
    }


    public String createAccessToken(Long userId, String userRole) { // 액세스 토큰 생성
        return jwtTokenProvider.createAccessToken(userId, userRole);
    }

    public JwtAuthenticationToken getAuthenticationByAccessToken(String accessToken) {

        // 1. 액세스 토큰 유효성 검증
        jwtTokenProvider.validateToken(accessToken); // 토큰 유효성 검증

        // 2. 액세스 토큰에서 클레임 추출
        Claims claims = jwtTokenProvider.getClaims(accessToken); // 클레임 추출

        // 3. 클레임에서 사용자 정보 추출
        Long userId = claims.get("userId", Long.class);
        String role = claims.get("role", String.class);

        // 4. 사용자 ID 및 액세스 토큰을 사용하여 JwtAuthentication Principal 생성
        JwtAuthentication principal = new JwtAuthentication(userId, accessToken);

        // 5. 사용자 역할 기반 Granted Authorities List 생성
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

        // 6. JwtAuthenticationToken 객체 생성
        return new JwtAuthenticationToken(principal, null, authorities);
    }

    public int getRefreshTokenExpirySeconds() {
        return refreshTokenExpirySeconds;
    }

    public int getAccessTokenExpirySeconds() {
        return accessTokenExpirySeconds;
    }

    private void checkRefreshToken(String refreshToken) {
        if (Objects.isNull(refreshToken) || refreshToken.isBlank()) {
            throw new NotFoundCookieException();
        }
    }

    public String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}