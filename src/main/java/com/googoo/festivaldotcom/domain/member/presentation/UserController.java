package com.googoo.festivaldotcom.domain.member.presentation;

import com.googoo.festivaldotcom.domain.member.application.dto.request.UpdateUserRequest;
import com.googoo.festivaldotcom.domain.member.application.dto.response.UserProfileResponse;
import com.googoo.festivaldotcom.domain.member.domain.service.UserService;
import com.googoo.festivaldotcom.global.auth.token.dto.jwt.JwtAuthentication;
import com.googoo.festivaldotcom.global.base.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping(value = "/{userId}", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponse<UserProfileResponse>> getUserProfile(
		@PathVariable Long userId
	) {
		UserProfileResponse response = userService.getUserProfile(userId);

		return ResponseEntity.ok().body(new ApiResponse<>(response));
	}

	@GetMapping(value = "/me", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponse<UserProfileResponse>> getMyProfile(
		@AuthenticationPrincipal JwtAuthentication user
	) {
		UserProfileResponse response = userService.getUserProfile(user.id());

		return ResponseEntity.ok().body(new ApiResponse<>(response));
	}

	@PutMapping(value = "/me", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponse<UserProfileResponse>> updateMyProfile(
		@RequestBody @Valid UpdateUserRequest updateUserRequest,
		@AuthenticationPrincipal JwtAuthentication user
	) {
		UserProfileResponse response = userService.updateUserProfile(updateUserRequest, user.id());

		return ResponseEntity.ok().body(new ApiResponse<>(response));
	}

	@DeleteMapping("/me")
	@ResponseStatus(NO_CONTENT)
	public void deleteMyProfile(
		@AuthenticationPrincipal JwtAuthentication user,
		@CookieValue("refreshToken") String refreshToken
	) {
		userService.deleteUser(user.id(), refreshToken);
	}

}
