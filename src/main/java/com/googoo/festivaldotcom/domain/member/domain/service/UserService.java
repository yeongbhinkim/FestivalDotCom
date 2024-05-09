package com.googoo.festivaldotcom.domain.member.domain.service;


import com.googoo.festivaldotcom.domain.member.application.dto.request.UpdateUserRequest;
import com.googoo.festivaldotcom.domain.member.application.dto.response.UserProfileResponse;
import com.googoo.festivaldotcom.domain.member.domain.model.ModifyForm;
import com.googoo.festivaldotcom.global.auth.oauth.dto.AuthUserInfo;
import com.googoo.festivaldotcom.global.auth.oauth.dto.OAuthUserInfo;

public interface UserService {

	/* 회원 등록 */
	AuthUserInfo getOrRegisterUser(OAuthUserInfo oauthUserInfo);

	/* 회원 프로필 조회 */
	UserProfileResponse getUserProfile(Long userId);

	/* 회원 프로필 수정 */
	UserProfileResponse updateUserProfile(UpdateUserRequest updateUserRequest, Long userId);

	/* 회원 탈퇴 */
	void deleteUser(Long userId, String refreshToken);

}
