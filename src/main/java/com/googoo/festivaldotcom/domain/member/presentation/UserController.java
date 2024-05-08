package com.googoo.festivaldotcom.domain.member.presentation;

import com.googoo.festivaldotcom.domain.member.application.dto.request.UpdateUserRequest;
import com.googoo.festivaldotcom.domain.member.application.dto.response.UserProfileResponse;
import com.googoo.festivaldotcom.domain.member.domain.service.UserService;
import com.googoo.festivaldotcom.global.auth.token.dto.jwt.JwtAuthentication;
import com.googoo.festivaldotcom.global.base.dto.ApiResponse;
import com.googoo.festivaldotcom.global.log.annotation.Trace;
import com.googoo.festivaldotcom.global.utils.DetermineUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Slf4j
@Controller
@RequestMapping("/api/v1/user")
// 이 컨트롤러의 모든 요청 URL이 '/api/v1/user'로 시작하도록 설정합니다.
@RequiredArgsConstructor
// Lombok 라이브러리를 사용하여 final 또는 @NonNull 필드에 대한 생성자를 자동으로 생성합니다. 이는 주입(Dependency Injection)을 위한 것입니다.

public class UserController {

	private final UserService userService;
	// UserService를 의존성 주입을 통해 컨트롤러에 연결합니다. UserService 클래스는 사용자 관련 비즈니스 로직을 처리합니다.

	@Trace
	@RequestMapping("/myPage")
	public String myPage(HttpServletRequest request
			, @AuthenticationPrincipal JwtAuthentication user
			, Model model) {
		log.info("info={}", "myPage() 호출 시작됨========================");

        log.info("user.id() = " + user.id());
		UserProfileResponse response = userService.getUserProfile(user.id());

//		UpdateUserRequest initialData = new UpdateUserRequest("", "", "");
		model.addAttribute("updateUserRequest", response);
		model.addAttribute("modifyForm", response);

		log.info("info={}", "myPage() 호출 종료됨========================");

		String view = DetermineUtil.determineView(request, "login/beforeLogin", "myPage/mypage");
		return view;
	}



//	@GetMapping(value = "/{userId}", produces = APPLICATION_JSON_VALUE)
//	// HTTP GET 요청을 '/api/v1/user/{userId}' URL로 매핑하고, 응답은 JSON 형식으로 반환합니다.
//	public ResponseEntity<ApiResponse<UserProfileResponse>> getUserProfile(@PathVariable Long userId) {
//		// 특정 사용자 ID를 받아 그 사용자의 프로필 정보를 조회합니다.
//		UserProfileResponse response = userService.getUserProfile(userId);
//		// UserService를 통해 사용자 프로필 정보를 가져옵니다.
//
//		return ResponseEntity.ok().body(new ApiResponse<>(response));
//		// ApiResponse 객체를 통해 UserProfileResponse 정보를 JSON 형태로 클라이언트에게 반환합니다.
//	}
//
//	//MyProfileEditPagec 참고
//	@GetMapping(value = "/me", produces = APPLICATION_JSON_VALUE)
//	// 현재 인증된 사용자의 프로필 정보를 반환하는 엔드포인트입니다.
//	public ResponseEntity<ApiResponse<UserProfileResponse>> getMyProfile(@AuthenticationPrincipal JwtAuthentication user) {
//		// 인증된 사용자의 정보를 JwtAuthentication 객체를 통해 가져옵니다.
//		UserProfileResponse response = userService.getUserProfile(user.id());
//		// 인증된 사용자의 ID를 사용하여 프로필 정보를 조회합니다.
//
//		return ResponseEntity.ok().body(new ApiResponse<>(response));
//		// 조회된 사용자 정보를 ApiResponse 객체를 통해 JSON 형태로 반환합니다.
//	}

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
}

