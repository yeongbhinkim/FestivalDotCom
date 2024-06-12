package com.googoo.festivaldotcom.function.member.infrastructure.mapper;

import com.googoo.festivaldotcom.function.member.application.dto.response.UserProfileResponse;
import com.googoo.festivaldotcom.function.member.domain.model.User;
import com.googoo.festivaldotcom.global.auth.oauth.dto.OAuthUserInfo;
import org.mapstruct.Mapper;

import static org.mapstruct.ReportingPolicy.*;

@Mapper(componentModel = "spring", unmappedSourcePolicy = IGNORE)
public interface UserMapper {

    UserProfileResponse toSingleUserResponse(User user);

    User toUser(OAuthUserInfo oauthUserInfo);


}
