package com.googoo.festivaldotcom.domain.member.infrastructure.repository;

import com.googoo.festivaldotcom.domain.member.application.dto.request.UpdateUserRequest;
import com.googoo.festivaldotcom.domain.member.domain.model.ModifyForm;
import com.googoo.festivaldotcom.domain.member.domain.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.Optional;

@Mapper
public interface UserRepository {
	Optional<User> findByUserIdByProviderAndOauthId(String provider, String oauthId);
	Optional<User> findById(@Param("id") Long id);

	void insertUser(User user);
//	int updateUserProfile(@Param("updateUserRequest") UpdateUserRequest updateUserRequest, @Param("userId") Long userId);
	int updateUserProfile(@Param("nickName") String nickName, @Param("profileImgUrl") String profileImgUrl, @Param("introduction") String introduction, @Param("id") Long id);

	int deleteUserProfile(@Param("userId") Long userId);


//	int updateUserProfile(ModifyForm modifyForm);
}
