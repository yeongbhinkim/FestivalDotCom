package com.googoo.festivaldotcom.global.auth.oauth.repository;

import com.googoo.festivaldotcom.global.utils.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Repository;

import java.net.URLEncoder;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.util.StringUtils.hasText;

/**
 * 이 클래스는 OAuth2 인증 요청을 쿠키에 저장하고 로드하는 기능을 제공하는 클래스입니다.
 * AuthorizationRequestRepository 인터페이스를 구현하여 OAuth2AuthorizationRequest를 저장, 로드, 제거합니다.
 */
@Slf4j
@Repository
public class HttpCookieOAuthAuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request"; // OAuth2 인증 요청을 저장하는 쿠키의 이름
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri"; // 리다이렉트 URI를 저장하는 쿠키의 이름
    private static final int COOKIE_EXPIRE_SECONDS = 180; // 쿠키의 유효기간(초)

    /**
     * 요청에서 OAuth2 인증 요청을 로드합니다.
     *
     * @param request HttpServletRequest 객체
     * @return OAuth2AuthorizationRequest 객체 또는 null
     */
    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        // 쿠키에서 OAuth2 인증 요청을 가져옴
        return CookieUtil.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
                .map(cookie -> CookieUtil.deserialize(cookie, OAuth2AuthorizationRequest.class))
                .orElse(null);
    }

    /**
     * OAuth2 인증 요청을 쿠키에 저장합니다.
     *
     * @param authorizationRequest OAuth2AuthorizationRequest 객체
     * @param request HttpServletRequest 객체
     * @param response HttpServletResponse 객체
     */
    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest,
                                         HttpServletRequest request, HttpServletResponse response) {
        // 인증 요청이 null인 경우 쿠키를 제거함
        if (authorizationRequest == null) {
            removeAuthorizationRequestCookies(request, response);
            return;
        }

        // 쿠키에 OAuth2 인증 요청을 저장함
        CookieUtil.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME,
                CookieUtil.serialize(authorizationRequest), COOKIE_EXPIRE_SECONDS);

        // 로그인 후 리다이렉트할 URI를 저장하는 쿠키를 추가함
        String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
        if (hasText(redirectUriAfterLogin)) {
            redirectUriAfterLogin = URLEncoder.encode(redirectUriAfterLogin, UTF_8);
            CookieUtil.addCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME, redirectUriAfterLogin, COOKIE_EXPIRE_SECONDS);
        }
    }

    /**
     * 요청에서 OAuth2 인증 요청을 제거합니다.
     *
     * @param request HttpServletRequest 객체
     * @param response HttpServletResponse 객체
     * @return OAuth2AuthorizationRequest 객체 또는 null
     */
    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        // 쿠키에서 OAuth2 인증 요청을 가져옴
        OAuth2AuthorizationRequest authRequest = this.loadAuthorizationRequest(request);

        // 인증 요청이 null이 아닌 경우 쿠키를 삭제함
        if (authRequest != null) {
            CookieUtil.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        }

        // 인증 요청을 반환함
        return authRequest;
    }

    /**
     * OAuth2 인증 요청 및 리다이렉트 URI 쿠키를 제거합니다.
     *
     * @param request HttpServletRequest 객체
     * @param response HttpServletResponse 객체
     */
    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        // OAuth2 인증 요청 쿠키를 삭제함
        CookieUtil.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        // 리다이렉트 URI 쿠키를 삭제함
        CookieUtil.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
    }
}
