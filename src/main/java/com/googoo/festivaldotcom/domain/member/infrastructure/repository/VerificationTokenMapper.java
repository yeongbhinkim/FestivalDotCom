package com.googoo.festivaldotcom.domain.member.infrastructure.repository;

import com.googoo.festivaldotcom.domain.member.domain.model.VerificationToken;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface VerificationTokenMapper {

    /**
     * 이메일과 토큰을 이용해 새로운 인증 토큰을 데이터베이스에 저장합니다.
     *
     * @param email 사용자 이메일
     * @param token 인증 토큰
     */
    void insertVerificationToken(@Param("email") String email, @Param("token") String token);

    /**
     * 주어진 토큰을 이용해 데이터베이스에서 인증 토큰을 찾습니다.
     *
     * @param token 인증 토큰
     * @return VerificationToken 인증 토큰과 관련된 데이터
     */
    VerificationToken findByToken(@Param("token") String token);

    /**
     * 이메일을 기준으로 데이터베이스에서 해당 이메일과 관련된 인증 토큰을 삭제합니다.
     *
     * @param email 사용자 이메일
     */
    void deleteByEmail(@Param("email") String email);
}
