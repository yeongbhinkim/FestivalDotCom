package com.googoo.festivaldotcom.domain.login.presentation;

import com.googoo.festivaldotcom.global.log.annotation.Trace;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class LoginController {

    @Trace
    @RequestMapping("/test0")
    public String test0(HttpServletRequest request) {
        log.info("info={}", "test0() 호출 시작됨========================");

        log.info("info={}", "test0() 호출 종료됨========================");

        String view = determineView(request, "login/beforeLogin", "login/afterLogin");
        return view;
    }

    @Trace
    @RequestMapping("/")
    public String home(HttpServletRequest request){
        log.info("info={}","home()호출됨");

        String view = determineView(request, "login/beforeLogin", "login/afterLogin");
        return view;
    }

    @Trace
    @RequestMapping("/oauthlogin")
    public String login() {
        log.info("info={}","oauthlogin()호출됨");
        return "/login/loginForm";
    }


    public String determineView(HttpServletRequest request, String beforeLoginView, String afterLoginView) {
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
