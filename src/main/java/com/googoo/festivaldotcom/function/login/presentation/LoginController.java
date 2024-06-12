package com.googoo.festivaldotcom.function.login.presentation;

import com.googoo.festivaldotcom.global.auth.token.dto.jwt.JwtAuthentication;
import com.googoo.festivaldotcom.global.log.annotation.Trace;
import com.googoo.festivaldotcom.global.utils.DetermineUtil;
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

//    private final UserService userService;

    @Trace
    @RequestMapping("/test0")
    public String test0(HttpServletRequest request
    , @AuthenticationPrincipal JwtAuthentication user
    , Model model) {
        log.info("info={}", "test0() 호출 시작됨========================");


//        log.info("user.id() = " + user.id());
        //        UpdateUserRequest initialData = userService.getUserProfile(user.id());
//        UserProfileResponse initialData = userService.getUserProfile(user.id());
//        UpdateUserRequest initialData = new UpdateUserRequest("", "", "","", "");
        model.addAttribute("updateUserRequest", "");
        log.info("info={}", "test0() 호출 종료됨========================");

        String view = DetermineUtil.determineView(request, "login/beforeLogin", "login/afterLoginUpdate");
        return view;
    }

    @Trace
    @RequestMapping("/")
    public String home(HttpServletRequest request){
        log.info("info={}","home()호출됨");

        String view = DetermineUtil.determineView(request, "login/beforeLogin", "login/afterLogin");
        return view;
    }

    @Trace
    @RequestMapping("/oauthlogin")
    public String login() {
        log.info("info={}","oauthlogin()호출됨");
        return "/login/loginForm";
    }





}
