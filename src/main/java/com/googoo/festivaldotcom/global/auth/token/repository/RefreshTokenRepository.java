package com.googoo.festivaldotcom.global.auth.token.repository;

import com.googoo.festivaldotcom.global.auth.token.model.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
