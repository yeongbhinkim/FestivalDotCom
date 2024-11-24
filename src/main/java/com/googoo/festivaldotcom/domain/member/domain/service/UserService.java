package com.googoo.festivaldotcom.domain.member.domain.service;


import com.googoo.festivaldotcom.domain.member.application.dto.request.UpdateUserRequest;
import com.googoo.festivaldotcom.domain.member.application.dto.response.UserProfileResponse;
import com.googoo.festivaldotcom.global.auth.oauth.dto.AuthUserInfo;
import com.googoo.festivaldotcom.global.auth.oauth.dto.OAuthUserInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.annotations.Param;

import java.io.IOException;

public interface UserService {

	/* 회원 등록 */
	AuthUserInfo getOauthId(OAuthUserInfo oauthUserInfo);

	/* 회원 프로필 조회 */
	UserProfileResponse getUser(Long userId);

	String getOauthId(Long userId);

	String getGender(Long userId);

	/* 회원 프로필 수정 */
	UserProfileResponse modifyUser(UpdateUserRequest updateUserRequest, Long userId) throws IOException;

	/* 회원 탈퇴 */
	void removeUser(HttpServletRequest request, HttpServletResponse response, Long userId, String oauthId, String refreshToken);
//	void removeUser(HttpServletRequest request, HttpServletResponse response, Long userId, String refreshToken);

	boolean getNickName(@Param("nickName") String nickName);
}
