package com.googoo.festivaldotcom.global.auth.token.dto.jwt;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * JWT 인증 토큰 클래스.
 * Spring Security의 AbstractAuthenticationToken을 상속받아 JWT 기반 인증을 구현.
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    // 인증된 사용자 정보를 저장하는 변수
    private final Object principal;

    // 인증 자격 증명을 저장하는 변수
    private String credentials;

    /**
     * JWT 인증 토큰 생성자.
     * 주로 인증이 완료된 후에 사용됨.
     *
     * @param principal   인증된 사용자 정보
     * @param authorities 사용자 권한 정보
     */
    public JwtAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        this(principal, null, authorities);
    }

    /**
     * JWT 인증 토큰 생성자.
     * 주로 인증 요청 시에 사용됨.
     *
     * @param principal   사용자 정보
     * @param credentials 자격 증명
     */
    public JwtAuthenticationToken(String principal, String credentials) {
        super(null);
        super.setAuthenticated(false); // 초기에는 인증되지 않은 상태로 설정

        this.principal = principal;
        this.credentials = credentials;
    }

    /**
     * JWT 인증 토큰 생성자.
     * 주로 인증이 완료된 후에 사용됨.
     *
     * @param principal   인증된 사용자 정보
     * @param credentials 자격 증명
     * @param authorities 사용자 권한 정보
     */
    public JwtAuthenticationToken(
            Object principal, String credentials, Collection<? extends GrantedAuthority> authorities
    ) {
        super(authorities);
        super.setAuthenticated(true); // 인증된 상태로 설정

        this.principal = principal;
        this.credentials = credentials;
    }

    /**
     * 인증된 사용자 정보를 반환하는 메서드.
     *
     * @return 인증된 사용자 정보
     */
    @Override
    public Object getPrincipal() {
        return principal;
    }

    /**
     * 자격 증명을 반환하는 메서드.
     *
     * @return 자격 증명
     */
    @Override
    public String getCredentials() {
        return credentials;
    }

    /**
     * 인증 상태를 설정하는 메서드.
     * 인증 상태를 true로 설정하려고 하면 IllegalArgumentException 예외를 던짐.
     *
     * @param isAuthenticated 인증 상태
     * @throws IllegalArgumentException 인증 상태가 true일 때 예외 발생
     */
    @Override
    public void setAuthenticated(boolean isAuthenticated) {
        if (isAuthenticated) {
            throw new IllegalArgumentException("유효하지 않은 접근입니다.");
        }
        super.setAuthenticated(false); // 인증 상태를 false로 설정
    }

    /**
     * 자격 증명을 지우는 메서드.
     * 보안을 위해 자격 증명을 null로 설정.
     */
    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        credentials = null; // 자격 증명 제거
    }

    /**
     * JWT 인증 토큰 정보를 문자열로 반환하는 메서드.
     *
     * @return JWT 인증 토큰 정보 문자열
     */
    @Override
    public String toString() {
        return "JwtAuthenticationToken{"
                + "principal=" + principal
                + ", credentials='" + credentials + '\''
                + '}';
    }
}
