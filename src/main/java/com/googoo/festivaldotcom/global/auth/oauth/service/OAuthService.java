package com.googoo.festivaldotcom.global.auth.oauth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class OAuthService {

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String naverClientSecret;

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
        String apiURL = "https://nid.naver.com/oauth2.0/token";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "delete");
        body.add("client_id", naverClientId);
        body.add("client_secret", naverClientSecret);
        body.add("access_token", accessToken);
        body.add("service_provider", "NAVER");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(apiURL, HttpMethod.POST, request, String.class);

        return response.getBody();
    }

    public String revokeKakaoToken(String accessToken) {
        String apiURL = "https://kapi.kakao.com/v1/user/unlink";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(apiURL, HttpMethod.POST, request, String.class);

        return response.getBody();
    }

    public String revokeGoogleToken(String accessToken) {
        String apiURL = "https://oauth2.googleapis.com/revoke";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("token", accessToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(apiURL, HttpMethod.POST, request, String.class);

        return response.getBody();
    }

    public void revokeToken(String provider, String accessToken) {
        switch (provider.toLowerCase()) {
            case "naver":
                revokeNaverToken(accessToken);
                break;
            case "kakao":
                revokeKakaoToken(accessToken);
                break;
            case "google":
                revokeGoogleToken(accessToken);
                break;
            default:
                throw new IllegalArgumentException("Unknown provider: " + provider);
        }
    }
}

