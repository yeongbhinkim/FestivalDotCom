package com.googoo.festivaldotcom.global.auth.token.filter;


import com.googoo.festivaldotcom.global.auth.token.dto.jwt.JwtAuthenticationToken;
import com.googoo.festivaldotcom.global.auth.token.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.secret-key}") // 비밀 키
    private String secretKey;

    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String accessToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    accessToken = cookie.getValue();
                    break;
                }
            }
        }

        if (accessToken != null) {
            try {
                // JWT 만료 시간 검증
                Claims claims = Jwts.parser()
                        .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                        .build()
                        .parseClaimsJws(accessToken)
                        .getBody();

                if (claims.getExpiration().before(new Date())) {
                    // refreshToken으로 accessToken 재발행
                    String refreshToken = null;
                    Cookie[] refreshTokenCookies = request.getCookies();
                    if (refreshTokenCookies != null) {
                        for (Cookie cookie : refreshTokenCookies) {
                            if ("refreshToken".equals(cookie.getName())) {
                                refreshToken = cookie.getValue();
                                break;
                            }
                        }
                    }
                    // refreshToken으로검증
                    accessToken = tokenService.getAccessTokensByRefreshToken(refreshToken);

                    // 새로 발행된 accessToken을 쿠키에 저장
                    Cookie newAccessTokenCookie = new Cookie("accessToken", accessToken);
                    newAccessTokenCookie.setHttpOnly(true); // 자바스크립트를 통한 접근 방지
                    newAccessTokenCookie.setSecure(true); // HTTPS를 통해서만 쿠키 전송
                    newAccessTokenCookie.setPath("/"); // 쿠키의 유효 경로 설정
                    response.addCookie(newAccessTokenCookie); // 응답에 쿠키 추가

                }

                // 유저 역할 추출
                String userRole = claims.get("role", String.class);

                // 쿠키 생성 및 설정
                Cookie roleCookie = new Cookie("userRole", userRole);
                roleCookie.setHttpOnly(false);  // 자바스크립트에서 접근 설정
                roleCookie.setPath("/");  // 쿠키가 전송되어야 할 경로
                roleCookie.setMaxAge(60 * 60); // 쿠키 유효 시간 설정 (예: 1시간)
                response.addCookie(roleCookie);  // 응답에 쿠키 추가

                // 액세스 토큰을 사용하여 인증 정보를 얻는다.
                JwtAuthenticationToken authentication = tokenService.getAuthenticationByAccessToken(accessToken);
                // SecurityContext에 인증 정보를 설정한다. 이를 통해 애플리케이션 전반에서 현재 사용자의 인증 정보를 접근할 수 있다.
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setHeader("Location", "/oauthlogin");
                return; // 필터 체인 중단
            }
        }

        // 요청을 계속 진행
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // 특정 경로("/tokens")에 대해서는 필터를 적용하지 않음
        return request.getRequestURI().endsWith("tokens");
    }
}
