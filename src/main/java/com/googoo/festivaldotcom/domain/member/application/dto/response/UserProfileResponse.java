package com.googoo.festivaldotcom.domain.member.application.dto.response;

public record UserProfileResponse(
	Long id,
	String nickname,
	String profileImgUrl,
	String introduction,
	Integer festivalCount,
	String mannerScore
) {
}
