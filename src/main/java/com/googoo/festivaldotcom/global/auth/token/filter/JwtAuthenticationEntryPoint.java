package com.googoo.festivaldotcom.global.auth.token.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.googoo.festivaldotcom.global.base.dto.ErrorResponse;
import com.googoo.festivaldotcom.global.base.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.CharEncoding;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * JWT 인증 진입 지점
 * 인증되지 않은 사용자가 보호된 리소스에 접근하려 할 때 호출됨.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // 로그 메시지 포맷
    private static final String ERROR_LOG_MESSAGE = "[ERROR] {} : {}";

    // JSON 직렬화를 위한 ObjectMapper
    private final ObjectMapper objectMapper;

    /**
     * 인증되지 않은 요청에 대한 응답을 생성하는 메서드.
     *
     * @param request       HttpServletRequest 객체
     * @param response      HttpServletResponse 객체
     * @param authException 인증 예외 객체
     * @throws IOException 입출력 예외 발생 시 던짐
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // 예외 로그 출력
        log.info(ERROR_LOG_MESSAGE, authException.getClass().getSimpleName(), authException.getMessage());

        // 응답 상태를 401 Unauthorized로 설정
        response.setStatus(UNAUTHORIZED.value());

        // 응답의 콘텐츠 타입을 JSON으로 설정
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // 응답의 문자 인코딩을 UTF-8로 설정
        response.setCharacterEncoding(CharEncoding.UTF_8);

        // ErrorResponse 객체를 JSON으로 직렬화하여 응답에 작성
        response.getWriter()
                .write(objectMapper.writeValueAsString(
                        ErrorResponse.of(ErrorCode.HANDLE_ACCESS_DENIED))
                );

        response.sendRedirect("/oauthlogin");
    }
}
