package com.googoo.festivaldotcom.domain.member.infrastructure.repository;

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
	void insertUser(User user);
	void updateUser(User user);

}
