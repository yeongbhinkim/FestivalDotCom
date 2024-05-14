package com.googoo.festivaldotcom.domain.member.infrastructure.mapper;

import com.googoo.festivaldotcom.domain.member.application.dto.response.UserProfileResponse;
import com.googoo.festivaldotcom.domain.member.domain.model.User;
import com.googoo.festivaldotcom.global.auth.oauth.dto.OAuthUserInfo;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;

import static org.mapstruct.ReportingPolicy.*;

@Mapper(componentModel = "spring", unmappedSourcePolicy = IGNORE)
public interface UserMapper {

    UserProfileResponse toSingleUserResponse(User user);

    User toUser(OAuthUserInfo oauthUserInfo);


}
