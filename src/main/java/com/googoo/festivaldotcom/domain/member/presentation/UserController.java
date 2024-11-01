package com.googoo.festivaldotcom.domain.member.presentation;

import com.googoo.festivaldotcom.domain.member.application.dto.request.EmailVerificationRequest;
import com.googoo.festivaldotcom.domain.member.application.dto.request.UpdateUserRequest;
import com.googoo.festivaldotcom.domain.member.application.dto.response.EmailVerificationResponse;
import com.googoo.festivaldotcom.domain.member.application.dto.response.TokenVerificationResponse;
import com.googoo.festivaldotcom.domain.member.application.dto.response.UserProfileResponse;
import com.googoo.festivaldotcom.domain.member.domain.model.OutForm;
import com.googoo.festivaldotcom.domain.member.domain.service.DomainValidationService;
import com.googoo.festivaldotcom.domain.member.domain.service.EmailVerificationService;
import com.googoo.festivaldotcom.domain.member.domain.service.UserService;
import com.googoo.festivaldotcom.global.auth.token.dto.jwt.JwtAuthentication;
import com.googoo.festivaldotcom.global.auth.token.service.JwtTokenProvider;
import com.googoo.festivaldotcom.global.auth.token.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/api/v1/user")
@Tag(name = "User API", description = "사용자 관련 기능 API (회원 정보 수정, 탈퇴, 이메일 인증)")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;
    private final JwtTokenProvider jwtTokenProvider;

    private final EmailVerificationService emailVerificationService;
    private final DomainValidationService domainValidationService;

    @Transactional
    @Operation(summary = "회원 수정 페이지", description = "사용자가 자신의 정보를 수정할 수 있는 페이지를 반환합니다.")
    @GetMapping("/myPage")
    public String myPage(@Parameter(description = "인증된 사용자 정보") @AuthenticationPrincipal JwtAuthentication user,
                         Model model) {
        UserProfileResponse modifyForm = userService.getUser(user.id());
        model.addAttribute("modifyForm", modifyForm);

        return "memberJoin/memberModifyPage";
    }

    @Operation(summary = "회원 정보 수정", description = "사용자가 자신의 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 정보가 성공적으로 수정됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 검증 오류 발생")
    })
    @PostMapping("/modify")
    public String modify(@Valid @ModelAttribute UpdateUserRequest modifyForm,
                         BindingResult bindingResult,
                         @Parameter(description = "인증된 사용자 정보") @AuthenticationPrincipal JwtAuthentication user,
                         Model model,
                         RedirectAttributes redirectAttributes) throws IOException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("modifyForm", modifyForm);
            model.addAttribute("org.springframework.validation.BindingResult.modifyForm", bindingResult);
            return "memberJoin/memberModifyPage";
        }

        UserProfileResponse userForm = userService.getUser(user.id());

            if (userService.getNickName(modifyForm.nickName()) && !userForm.nickName().equals(modifyForm.nickName())) {
                bindingResult.rejectValue("nickName", "duplicate", "이미 사용 중인 닉네임입니다.");
                model.addAttribute("modifyForm", modifyForm);
                model.addAttribute("org.springframework.validation.BindingResult.modifyForm", bindingResult);
                return "memberJoin/memberModifyPage";
            }

        // 성공 메시지를 Flash Attribute로 설정
//        redirectAttributes.addFlashAttribute("successMessage", "사용자 정보가 성공적으로 수정되었습니다.");

        userService.setUser(modifyForm, user.id());
        return "redirect:/api/v1/user/myPage";
    }

    @Operation(summary = "회원 탈퇴 페이지", description = "회원 탈퇴를 위한 페이지로 이동합니다.")
    @GetMapping("/myPageRemove")
    public String myPageRemove(HttpServletRequest request,
                               OutForm outForm,
                               Model model) {
        model.addAttribute("outForm", outForm);
        return "memberJoin/memberDelpage";
    }

    @Operation(summary = "회원 탈퇴 처리", description = "회원 탈퇴를 처리하고, 관련 토큰을 무효화합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 탈퇴가 성공적으로 처리됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 검증 오류 발생")
    })
    @PostMapping("/remove")
    public String delete(HttpServletRequest request,
                         HttpServletResponse response,
                         @Valid @ModelAttribute OutForm outForm,
                         BindingResult bindingResult,
                         @AuthenticationPrincipal JwtAuthentication user,
                         @CookieValue("refreshToken") String refreshToken,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("outForm", outForm);
            return "memberDelPage";
        }

        if (!outForm.isAgree()) {
            bindingResult.rejectValue("agree", "error.outForm", "탈퇴 안내 사항에 동의해야 합니다.");
            model.addAttribute("outForm", outForm);
            return "memberDelPage";
        }

        String oauthId = userService.getOauthId(user.id());
        userService.removeUser(request, response, user.id(), oauthId, refreshToken);

        SecurityContextHolder.clearContext();
        request.getSession().invalidate();

        String token = tokenService.extractTokenFromRequest(request);
        if (token != null) {
            jwtTokenProvider.invalidateToken(token);
        }

        return "login/loginPage";
    }

    @Operation(summary = "이메일 인증 요청", description = "사용자의 이메일로 인증 링크를 전송합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 인증 링크가 성공적으로 전송됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 이메일 형식 또는 도메인 유효하지 않음")
    })
    @PostMapping("/sendVerificationEmail")
    public ResponseEntity<EmailVerificationResponse> sendVerificationEmail(
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "인증 요청에 필요한 사용자 이메일") EmailVerificationRequest request,
            @Parameter(description = "인증된 사용자 정보") @AuthenticationPrincipal JwtAuthentication user) {

        // 이메일에서 도메인 부분 추출
        String email = request.getEmail();
        String domain = extractDomainFromEmail(email);

        // 도메인 유효성 확인
        if (!domainValidationService.isDomainValid(domain)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new EmailVerificationResponse(false, "유효하지 않은 도메인입니다."));
        }

        // 차단된 도메인인지 확인
        if (domainValidationService.isBlockedDomain(domain)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new EmailVerificationResponse(false, "일반 메일 도메인은 사용할 수 없습니다."));
        }

        // 도메인이 유효하면 이메일 전송
        emailVerificationService.sendVerificationEmail(email, user.id().toString());
        return ResponseEntity.ok(new EmailVerificationResponse(true, "이메일 인증 링크가 전송되었습니다."));
    }

    // 이메일에서 도메인 부분 추출
    private String extractDomainFromEmail(String email) {
        String[] parts = email.split("@");
        return parts.length == 2 ? parts[1] : "";
    }


    @Operation(summary = "이메일 인증 처리", description = "사용자가 이메일로 받은 토큰을 검증하여 인증을 처리합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 인증이 성공적으로 처리됨"),
            @ApiResponse(responseCode = "400", description = "유효하지 않거나 만료된 토큰")
    })
    @GetMapping("/verify")
    public ResponseEntity<TokenVerificationResponse> verifyEmail(@RequestParam("token") String token) {
        boolean isVerified = emailVerificationService.verifyEmailToken(token);

        if (isVerified) {
            return ResponseEntity.ok(new TokenVerificationResponse(true, "이메일 인증이 성공했습니다."));
        } else {
            return ResponseEntity.badRequest().body(new TokenVerificationResponse(false, "유효하지 않거나 만료된 토큰입니다."));
        }
    }
}
