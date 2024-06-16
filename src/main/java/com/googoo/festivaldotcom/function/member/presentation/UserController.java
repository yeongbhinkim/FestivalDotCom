package com.googoo.festivaldotcom.function.member.presentation;

import com.googoo.festivaldotcom.function.member.application.dto.request.UpdateUserRequest;
import com.googoo.festivaldotcom.function.member.application.dto.response.UserProfileResponse;
import com.googoo.festivaldotcom.function.member.domain.model.OutForm;
import com.googoo.festivaldotcom.function.member.domain.model.User;
import com.googoo.festivaldotcom.function.member.domain.service.UserService;
import com.googoo.festivaldotcom.global.auth.token.dto.jwt.JwtAuthentication;
import com.googoo.festivaldotcom.global.auth.token.service.JwtTokenProvider;
import com.googoo.festivaldotcom.global.auth.token.service.TokenService;
import com.googoo.festivaldotcom.global.log.annotation.Trace;
import com.googoo.festivaldotcom.global.utils.DetermineUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;


@Slf4j
@Controller
@RequestMapping("/api/v1/user")
// 이 컨트롤러의 모든 요청 URL이 '/api/v1/user'로 시작하도록 설정합니다.
@RequiredArgsConstructor
// Lombok 라이브러리를 사용하여 final 또는 @NonNull 필드에 대한 생성자를 자동으로 생성합니다. 이는 주입(Dependency Injection)을 위한 것입니다.

public class UserController {

    private final UserService userService;
    private final TokenService tokenService;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${attach.root_dir}")
    private String ROOT_DIR;

    @Value("${attach.handler}")
    private String HANDLER;

    /**
     * 회원 수정 화면
     *
     * @param request
     * @param user
     * @param model
     * @return
     */
    @Trace
    @RequestMapping("/myPage")
    public String myPage(HttpServletRequest request
            , @AuthenticationPrincipal JwtAuthentication user
            , Model model) {
        UserProfileResponse modifyForm = userService.getUser(user.id());

        model.addAttribute("modifyForm", modifyForm);

        String view = DetermineUtil.determineView(request, "login/beforeLogin", "memberJoin/memberModifypage");
        return view;
    }

    /**
     * 회원 정보 수정
     *
     * @param modifyForm
     * @param user
     * @return
     * @throws IOException
     */
    @Trace
    @PostMapping("/modify")
    public String modify(@Valid @ModelAttribute UpdateUserRequest modifyForm,
                         BindingResult bindingResult,
                         @AuthenticationPrincipal JwtAuthentication user,
                         Model model) throws IOException {
        // 검증 결과 처리
        if (bindingResult.hasErrors()) {
            // 검증 오류가 있으면 폼을 다시 보여줌
            model.addAttribute("modifyForm", modifyForm);
            model.addAttribute("org.springframework.validation.BindingResult.modifyForm", bindingResult);
            return "memberJoin/memberModifypage";
        }

        // 검증 로직 추가 (예: 닉네임 중복 체크)
        if (userService.getNickName(modifyForm.nickName())) {
            bindingResult.rejectValue("nickName", "duplicate", "이미 사용 중인 닉네임입니다.");
            model.addAttribute("modifyForm", modifyForm);
            model.addAttribute("org.springframework.validation.BindingResult.modifyForm", bindingResult);
            return "memberJoin/memberModifypage";
        }

        userService.setUser(modifyForm, user.id());
        return "redirect:/api/v1/user/myPage";
    }

    /**
     * 회원 탈퇴 페이지
     *
     * @param request
     * @return
     */
    @Trace
    @RequestMapping("/myPageRemove")
    public String myPageRemove(HttpServletRequest request,
                               OutForm outForm,
                               Model model
    ) {

        //탈퇴 동의 페이지로 이동
        model.addAttribute("outForm", outForm);

        String view = DetermineUtil.determineView(request, "login/beforeLogin", "memberJoin/memberDelpage");
        return view;
    }

    @Trace
    @PostMapping("/remove")
    public String delete(HttpServletRequest request,
                         HttpServletResponse response,
                         @Valid @ModelAttribute OutForm outForm,
                         BindingResult bindingResult,
                         @AuthenticationPrincipal JwtAuthentication user,
                         @CookieValue("refreshToken") String refreshToken,
                         Model model) {
        // 검증 결과 처리
        if (bindingResult.hasErrors()) {
            model.addAttribute("outForm", outForm);
            return "memberJoin/memberDelpage";  // 다시 탈퇴 페이지로 이동
        }

        // 체크박스가 체크되지 않았으면 에러 메시지 처리
        if (!outForm.isAgree()) {
            bindingResult.rejectValue("agree", "error.outForm", "탈퇴 안내 사항에 동의해야 합니다.");
            model.addAttribute("outForm", outForm);
            return "memberJoin/memberDelpage";  // 다시 탈퇴 페이지로 이동
        }
        //조회해서 준다
        String oauthId = userService.getOauthId(user.id());

        // JwtAuthentication 객체를 통해 현재 인증된 사용자의 ID와, 쿠키에서 가져온 refreshToken을 사용하여 사용자를 삭제합니다.
        userService.removeUser(request, response, user.id(),oauthId, refreshToken);

        // JwtAuthentication 객체를 통해 현재 인증된 사용자의 ID와, 쿠키에서 가져온 refreshToken을 사용하여 사용자를 삭제합니다.
//        userService.removeUser(request, response, user.id(), refreshToken);

        // SecurityContext 초기화
        SecurityContextHolder.clearContext();

        // 세션 무효화
        request.getSession().invalidate();

        // JWT 토큰 무효화
        String token = tokenService.extractTokenFromRequest(request);
        if (token != null) {
            jwtTokenProvider.invalidateToken(token);
        }

        return "login/beforeLogin";
    }

}



