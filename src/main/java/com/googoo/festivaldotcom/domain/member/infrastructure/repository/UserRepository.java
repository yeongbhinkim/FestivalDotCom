package com.googoo.festivaldotcom.domain.member.infrastructure.repository;

import com.googoo.festivaldotcom.domain.member.application.dto.response.Gender;
import com.googoo.festivaldotcom.domain.member.domain.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface UserRepository {
    Optional<User> selectOauthId(String provider, String oauthId);

    Optional<User> selectId(@Param("id") Long id);

    String selectGender(@Param("id") Long id);

    void insertUser(User user);

    int updateUser(@Param("nickName") String nickName, @Param("profileImgUrl") String profileImgUrl, @Param("introduction") String introduction, @Param("gender") Gender gender, @Param("companyEmail") String companyEmail, @Param("id") Long id);

    int updateUserEmail(@Param("companyEmail") String companyEmail, @Param("id") Long id);

    int deleteUser(@Param("userId") Long userId);

    String getOauthId(@Param("userId") Long userId);

	boolean selectNickName(@Param("nickName") String nickName);
}
