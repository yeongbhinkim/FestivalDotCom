package com.googoo.festivaldotcom.domain.login.presentation;

import com.googoo.festivaldotcom.domain.member.application.dto.request.UpdateUserRequest;
import com.googoo.festivaldotcom.domain.member.application.dto.response.UserProfileResponse;
import com.googoo.festivaldotcom.domain.member.domain.service.UserService;
import com.googoo.festivaldotcom.domain.member.infrastructure.repository.UserRepository;
import com.googoo.festivaldotcom.global.auth.token.dto.jwt.JwtAuthentication;
import com.googoo.festivaldotcom.global.log.annotation.Trace;
import com.googoo.festivaldotcom.global.utils.DetermineUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    /**
     *  홈 화면
     * @param request
     * @return
     */
    @Trace
    @RequestMapping("/")
    public String home(HttpServletRequest request) {
        log.info("info={}", "home()호출됨");

        String view = DetermineUtil.determineView(request, "login/beforeLogin", "login/afterLogin");
        return view;
    }

    /**
     * 로그인 화면
     * @return
     */
    @Trace
    @RequestMapping("/oauthlogin")
    public String login() {
        log.info("info={}", "oauthlogin()호출됨");
        return "/login/loginForm";
    }


}
