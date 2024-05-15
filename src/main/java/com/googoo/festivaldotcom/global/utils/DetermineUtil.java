package com.googoo.festivaldotcom.global.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class DetermineUtil {

    public static String determineView(HttpServletRequest request, String beforeLoginView, String afterLoginView) {
        Cookie[] cookies = request.getCookies();
        String view = beforeLoginView;  // 기본적으로 로그인 전 화면으로 설정

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    view = afterLoginView;  // refreshToken 쿠키가 있으면 로그인 후 화면으로 설정
                    break;
                }
            }
        }
        return view;
    }

}
