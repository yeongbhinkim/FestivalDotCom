package com.googoo.festivaldotcom.global.auth.oauth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OAuthService {

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String naverClientSecret;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String kakaoClientSecret;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;

    private final RestTemplate restTemplate;

    @Autowired
    public OAuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String revokeNaverToken(String accessToken) {

        return "https://nid.naver.com/nidlogin.logout";
    }

    public String revokeKakaoToken(String accessToken) {
        String apiURL = "https://kauth.kakao.com/oauth/logout";
        String logoutRedirectUri = "http://localhost:8080/oauthlogin";
//        String logoutRedirectUri = "http://220.118.225.167:2221/oauthlogin";

        // URL에 필요한 파라미터 추가
        String logoutUrl = String.format("%s?client_id=%s&logout_redirect_uri=%s", apiURL, kakaoClientId, logoutRedirectUri);

        return logoutUrl;
    }

    public String revokeGoogleToken(String accessToken) {

        return "https://accounts.google.com/Logout";
    }

    public String revokeToken(String provider, String accessToken) {
        switch (provider.toLowerCase()) {
            case "naver":
                return revokeNaverToken(accessToken);
            case "kakao":
                return revokeKakaoToken(accessToken);
            case "google":
                return revokeGoogleToken(accessToken);
            default:
                throw new IllegalArgumentException("Unknown provider: " + provider);
        }
    }
}

