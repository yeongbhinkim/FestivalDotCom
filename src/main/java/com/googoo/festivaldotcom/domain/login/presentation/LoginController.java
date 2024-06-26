package com.googoo.festivaldotcom.domain.login.presentation;

import com.googoo.festivaldotcom.domain.member.domain.model.User;
import com.googoo.festivaldotcom.domain.member.infrastructure.repository.UserRepository;
import com.googoo.festivaldotcom.global.auth.oauth.service.OAuthService;
import com.googoo.festivaldotcom.global.auth.token.dto.jwt.JwtAuthentication;
import com.googoo.festivaldotcom.global.log.annotation.Trace;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final OAuthService oAuthService;
    private final UserRepository userRepository;


    /**
     *  홈 화면
     * @param request
     * @return
     */
    @Trace
    @RequestMapping("/")
    public String home(HttpServletRequest request
    ,@AuthenticationPrincipal JwtAuthentication user
    ) {

//        String view = DetermineUtil.determineView(request, "login/beforeLogin", "login/afterLogin");
        return "login/loginPage";
    }

    /**
     * 로그인 화면
     * @return
     */
    @Trace
    @RequestMapping("/oauthlogin")
    public String login() {
        log.info("oauthlogin = {}", "oauthlogin()호출됨");

        return "login/loginPage";
    }

    /**
     * 로그아웃 화면
     * @return
     */
    @Trace
    @GetMapping("/oauthlogOut")
    public ResponseEntity<String> logOut(
            @AuthenticationPrincipal JwtAuthentication user
    ) {
        log.info("oauthlogOut = {}", "oauthlogOut()호출됨");

        Optional<User> userData = userRepository.selectId(user.id());

        String Provider = userData.get().getProvider();

        // OAuth 로그아웃 URL 가져오기
        String logoutUrl =oAuthService.revokeToken(Provider, user.accessToken());

        return ResponseEntity.ok(logoutUrl);
    }


}
