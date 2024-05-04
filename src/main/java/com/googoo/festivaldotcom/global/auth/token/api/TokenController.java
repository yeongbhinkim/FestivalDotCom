package com.googoo.festivaldotcom.global.auth.token.api;

import com.googoo.festivaldotcom.global.auth.token.dto.response.TokenResponse;
import com.googoo.festivaldotcom.global.auth.token.service.TokenService;
import com.googoo.festivaldotcom.global.utils.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

/**
 * 토큰 관련 API를 처리하는 컨트롤러.
 */
@RestController
@RequiredArgsConstructor // Lombok을 사용하여 생성자 주입을 자동화
@RequestMapping("/api/v1/tokens") // 해당 컨트롤러의 기본 URI 설정
public class TokenController {

    private final TokenService tokenService; // 토큰 서비스 의존성 주입

    /**
     * 새로운 액세스 토큰을 발급받기 위한 엔드포인트.
     * @param refreshToken 쿠키에서 받아온 리프레시 토큰
     * @return 새로 발급받은 액세스 토큰과 함께 200 OK 응답
     */
    @PostMapping
    public ResponseEntity<TokenResponse> refreshAccessToken(
            @CookieValue("refreshToken") String refreshToken // 리프레시 토큰을 쿠키에서 추출
    ) {
        // 토큰 서비스를 이용하여 새 액세스 토큰을 받아옴
        String accessToken = tokenService.getAccessTokensByRefreshToken(refreshToken);

        // 새 토큰과 함께 응답 반환
        return ResponseEntity.ok()
                .body(new TokenResponse(accessToken));
    }

    /**
     * 리프레시 토큰을 만료시키는 엔드포인트.
     * @param refreshToken 쿠키에서 받아온 리프레시 토큰
     * @return 빈 쿠키를 설정하여 리프레시 토큰을 클라이언트에서 제거하고 204 No Content 응답
     */
    @DeleteMapping
    public ResponseEntity<Void> expireRefreshToken(
            @CookieValue("refreshToken") String refreshToken // 리프레시 토큰을 쿠키에서 추출
    ) {
        // 토큰 서비스를 이용하여 리프레시 토큰 삭제
        tokenService.deleteRefreshToken(refreshToken);

        // 쿠키를 비워 리프레시 토큰을 클라이언트에서 제거
        ResponseCookie emptyCookie = CookieUtil.getEmptyCookie("refreshToken");

        // 쿠키를 비워서 응답 헤더에 설정하고, 204 No Content 응답 반환
        return ResponseEntity.noContent()
                .header(SET_COOKIE, emptyCookie.toString()).build();
    }
}
