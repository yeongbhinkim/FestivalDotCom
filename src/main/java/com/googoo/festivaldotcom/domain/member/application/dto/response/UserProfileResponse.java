package com.googoo.festivaldotcom.domain.member.application.dto.response;

import com.nimbusds.openid.connect.sdk.claims.Gender;

public record UserProfileResponse(
	Long id,
	String nickName,
	String profileImgUrl,
	String introduction,
	Integer festivalCount,
	String mannerScore,
	Gender gender,
	String companyEmail
) {
}
