package com.googoo.festivaldotcom.domain.member.infrastructure.repository;

import com.googoo.festivaldotcom.domain.member.application.dto.request.UpdateUserRequest;
import com.googoo.festivaldotcom.domain.member.domain.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface UserRepository {
	Optional<User> findByUserIdByProviderAndOauthId(String provider, String oauthId);
	Optional<User> findById(@Param("id") Long id);

	void insertUser(User user);
	int updateUserProfile(@Param("updateUserRequest") UpdateUserRequest updateUserRequest, @Param("userId") Long userId);
	int deleteUserProfile(@Param("userId") Long userId);

}
