package com.googoo.festivaldotcom.global.auth.oauth.handler;

import com.googoo.festivaldotcom.global.auth.oauth.repository.HttpCookieOAuthAuthorizationRequestRepository;
import com.googoo.festivaldotcom.global.utils.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URLDecoder;

import static com.googoo.festivaldotcom.global.auth.oauth.repository.HttpCookieOAuthAuthorizationRequestRepository.*;
import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthAuthenticationFailureHandler
        extends SimpleUrlAuthenticationFailureHandler {

    private static final String DEFAULT_TARGET_URL = "/";
    private final HttpCookieOAuthAuthorizationRequestRepository httpCookieOAuthAuthorizationRequestRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        String redirectUrl = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .map(cookie -> URLDecoder.decode(cookie, UTF_8))
                .orElse(DEFAULT_TARGET_URL);

        String targetUrl = UriComponentsBuilder.fromUriString(redirectUrl)
                .queryParam("error", exception.getMessage()) // TODO: exception.getMessage() -> error code
                .build().toUriString();

        httpCookieOAuthAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}


