package com.googoo.festivaldotcom.domain.member.domain.service;


import com.googoo.festivaldotcom.domain.member.application.dto.request.UpdateUserRequest;
import com.googoo.festivaldotcom.domain.member.application.dto.response.UserProfileResponse;
import com.googoo.festivaldotcom.domain.member.domain.exception.UserNotFoundException;
import com.googoo.festivaldotcom.domain.member.domain.model.User;
import com.googoo.festivaldotcom.domain.member.infrastructure.mapper.UserMapper;
import com.googoo.festivaldotcom.domain.member.infrastructure.repository.UserRepository;
import com.googoo.festivaldotcom.global.auth.oauth.dto.AuthUserInfo;
import com.googoo.festivaldotcom.global.auth.oauth.dto.OAuthUserInfo;
import com.googoo.festivaldotcom.global.auth.token.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultUserService implements UserService {

	public static final String DEFAULT_ROLE = "ROLE_USER";

	private final UserMapper userMapper;
	private final UserRepository userRepository;
	private final TokenService tokenService;

	/* [회원 인증 정보 조회 및 저장] 등록된 유저 정보 찾아서 제공하고 없으면 등록합니다. */
	@Override
	@Transactional
	@Cacheable(value = "User", key = "#oauthUserInfo.oauthId")
	public AuthUserInfo getOrRegisterUser(OAuthUserInfo oauthUserInfo) {
		Optional<User> optionalUser = userRepository.findByUserIdByProviderAndOauthId(oauthUserInfo.provider(), oauthUserInfo.oauthId());

		User user = optionalUser.orElseGet(() -> {
			// 새로운 User 객체를 생성하고 데이터베이스에 저장
			User newUser = userMapper.toUser(oauthUserInfo);
			userRepository.insertUser(newUser);
			return userRepository.findByUserIdByProviderAndOauthId(oauthUserInfo.provider(), oauthUserInfo.oauthId())
					.orElseThrow(() -> new IllegalStateException("User could not be created"));
		});

//		if(optionalUser.isPresent()) {
//			// 기존 사용자 정보를 갱신
//			userRepository.updateUser(user);
//		}

		log.info("Mapped User: {}", user);
		log.info("DEFAULT_ROLE: {}", DEFAULT_ROLE);

		return new AuthUserInfo(user.getId(), DEFAULT_ROLE, user.getNickname());
	}


	/* [회원 조회] 사용자 ID를 통해 등록된 유저 정보 찾아서 제공하고 없으면 예외가 발생합니다. */
	@Override
	@Cacheable(value = "User", key = "#userId")
	public UserProfileResponse getUserProfile(Long userId) {
		return userRepository.findById(userId)
			.map(userMapper::toSingleUserResponse)
			.orElseThrow(() -> new UserNotFoundException(userId));
	}

	/* [회원 프로필 수정] UpdateUserRequest DTO를 사용해서 사용자의 프로필(닉네임, 프로필 이미지, 자기소개)를 한번에 수정합니다. */

	@Override
	@Transactional
	@CachePut(value = "User", key = "#userId")
	public UserProfileResponse updateUserProfile(UpdateUserRequest updateUserRequest, Long userId) {
		 userRepository.updateUserProfile(updateUserRequest, userId);

		return userRepository.findById(userId)
				.map(user -> userMapper.toSingleUserResponse(user))  // User를 UserProfileResponse로 변환
				.orElseThrow(() -> new UserNotFoundException(userId));
	}

//	@Override
//	@Transactional
//	@CachePut(value = "User", key = "#userId")
//	public UserProfileResponse updateUserProfile(UpdateUserRequest updateUserRequest, Long userId) {
//		return userRepository.findById(userId)
//			.map(user -> user.changeProfile(updateUserRequest))
//			.map(userMapper::toSingleUserResponse)
//			.orElseThrow(() -> new UserNotFoundException(userId));
//	}

	/* [회원 탈퇴] 계정을 삭제합니다. soft delete가 적용됩니다.*/
	@Override
	@Transactional
	@CacheEvict(value = "User", key = "#userId")
	public void deleteUser(Long userId, String refreshToken) {

		userRepository.findById(userId)
			.ifPresentOrElse(user -> {
				user.deleteInfo();
				tokenService.deleteRefreshToken(refreshToken);
			}, () -> {
				throw new UserNotFoundException(userId);
			});

		//탈퇴 로직 추가
		userRepository.deleteUserProfile(userId);
	}
}
