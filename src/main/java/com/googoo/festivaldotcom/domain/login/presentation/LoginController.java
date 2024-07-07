package com.googoo.festivaldotcom.domain.login.presentation;

import com.googoo.festivaldotcom.domain.festival.application.applicationService.FestivalApplicationService;
import com.googoo.festivaldotcom.domain.festival.application.dto.response.GetFestival;
import com.googoo.festivaldotcom.domain.festival.domain.model.Festival;
import com.googoo.festivaldotcom.domain.member.domain.model.User;
import com.googoo.festivaldotcom.domain.member.infrastructure.repository.UserRepository;
import com.googoo.festivaldotcom.global.auth.oauth.service.OAuthService;
import com.googoo.festivaldotcom.global.auth.token.dto.jwt.JwtAuthentication;
import com.googoo.festivaldotcom.global.log.annotation.Trace;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final OAuthService oAuthService;
    private final UserRepository userRepository;
    // FestivalApplicationService를 주입받음
    private final FestivalApplicationService festivalApplicationService;


    /**
     * 홈 화면
     *
     * @return
     */
    @Trace
    @RequestMapping("/")
    public String home(
            @ModelAttribute GetFestival getFestival,
            Model model
            ) {

        log.info("Receiving request to get festivals: {}", getFestival);

        List<Festival> festivals = festivalApplicationService.getFestival(getFestival);

        log.info("Retrieved {} festivals", festivals.size());

        model.addAttribute("festivals", festivals);
        model.addAttribute("getFestival", getFestival);

        return "festival/festivalPage";
    }

    /**
     * 로그인 화면
     *
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
     *
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
        String logoutUrl = oAuthService.revokeToken(Provider, user.accessToken());

        return ResponseEntity.ok(logoutUrl);
    }


}
