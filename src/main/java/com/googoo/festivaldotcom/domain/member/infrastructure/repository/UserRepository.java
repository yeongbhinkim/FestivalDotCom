package com.googoo.festivaldotcom.domain.member.infrastructure.repository;

import com.googoo.festivaldotcom.domain.member.application.dto.request.UpdateUserRequest;
import com.googoo.festivaldotcom.domain.member.domain.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserRepository {
	Optional<User> findByUserIdByProviderAndOauthId(String provider, String oauthId);
	List<User> findRandom(int count);
	Optional<User> findById(@Param("id") Long id);

	int updateUserProfile(@Param("updateUserRequest") UpdateUserRequest updateUserRequest, @Param("userId") Long userId);

	void insertUser(User user);
	void updateUser(User user);

}
