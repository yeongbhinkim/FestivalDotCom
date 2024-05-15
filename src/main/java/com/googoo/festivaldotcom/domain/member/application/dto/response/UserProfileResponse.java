package com.googoo.festivaldotcom.domain.member.application.dto.response;

public record UserProfileResponse(
	Long id,
	String nickName,
	String profileImgUrl,
	String introduction,
	Integer festivalCount,
	String mannerScore
) {
}
