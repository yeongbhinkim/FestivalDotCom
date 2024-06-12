package com.googoo.festivaldotcom.function.member.presentation;

import com.googoo.festivaldotcom.function.member.application.dto.request.UpdateUserRequest;
import com.googoo.festivaldotcom.function.member.application.dto.response.UserProfileResponse;
import com.googoo.festivaldotcom.function.member.domain.service.UserService;
import com.googoo.festivaldotcom.global.auth.token.dto.jwt.JwtAuthentication;
import com.googoo.festivaldotcom.global.log.annotation.Trace;
import com.googoo.festivaldotcom.global.utils.DetermineUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;


@Slf4j
@Controller
@RequestMapping("/api/v1/user")
// 이 컨트롤러의 모든 요청 URL이 '/api/v1/user'로 시작하도록 설정합니다.
@RequiredArgsConstructor
// Lombok 라이브러리를 사용하여 final 또는 @NonNull 필드에 대한 생성자를 자동으로 생성합니다. 이는 주입(Dependency Injection)을 위한 것입니다.

public class UserController {

    private final UserService userService;

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
     * 프로필사진 첨부파일
     * 공통으로 뺴야 됨.
     *
     */
    @Configuration
    public class WebConfig implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/profileImgUrl/**")
                    .addResourceLocations("file:///C:/profileImgUrl/");
        }
    }


}


//	@PutMapping(value = "/me", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
//	// 현재 인증된 사용자의 프로필 정보를 업데이트하는 HTTP PUT 요청을 처리합니다.
//	public ResponseEntity<ApiResponse<UserProfileResponse>> updateMyProfile(
//			@RequestBody @Valid UpdateUserRequest updateUserRequest,
//			@AuthenticationPrincipal JwtAuthentication user
//	) {
//		// 요청 본문에서 전달된 UpdateUserRequest 객체를 사용하여 사용자 정보를 업데이트합니다. 이 객체는 @Valid 어노테이션을 통해 검증됩니다.
//		UserProfileResponse response = userService.updateUserProfile(updateUserRequest, user.id());
//		// 업데이트된 사용자 프로필 정보를 가져와 반환합니다.
//
//		return ResponseEntity.ok().body(new ApiResponse<>(response));
//		// 업데이트된 프로필 정보를 ApiResponse 객체를 통해 JSON 형태로 반환합니다.
//	}

//	@PostMapping("/me")
//	public String updateProfile(@Valid @ModelAttribute UpdateUserRequest request, BindingResult result, RedirectAttributes redirectAttributes
//	,@AuthenticationPrincipal JwtAuthentication user) {
//		// 폼 데이터 유효성 검사
//		if (result.hasErrors()) {
//			// 유효성 검사 실패 시, 다시 프로필 수정 페이지로 이동
//			return "userUpdate";
//		}
//
//		try {
//			// UserService를 통해 사용자 프로필 업데이트 시도
//			UserProfileResponse response = userService.updateUserProfile(request, user.id());
//			// 성공 메시지 설정
//			redirectAttributes.addFlashAttribute("successMessage", "프로필이 성공적으로 업데이트되었습니다.");
//		} catch (Exception e) {
//			// 업데이트 실패 시 에러 메시지 설정
//			redirectAttributes.addFlashAttribute("errorMessage", "프로필 업데이트에 실패했습니다.");
//		}
//		// 리디렉션을 통해 페이지를 다시 로드함
//		return "redirect:/userUpdate";
//	}

//	@DeleteMapping("/me")
//	@ResponseStatus(NO_CONTENT)
//	// 현재 인증된 사용자의 프로필을 삭제하는 HTTP DELETE 요청을 처리합니다.
//	public void deleteMyProfile(
//			@AuthenticationPrincipal JwtAuthentication user,
//			@CookieValue("refreshToken") String refreshToken
//	) {
//		// JwtAuthentication 객체를 통해 현재 인증된 사용자의 ID와, 쿠키에서 가져온 refreshToken을 사용하여 사용자를 삭제합니다.
//		userService.deleteUser(user.id(), refreshToken);
//		// 사용자 삭제 요청을 처리합니다.
//	}


