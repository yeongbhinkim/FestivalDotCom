package com.googoo.festivaldotcom.domain.member.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest(@NotBlank String nickName,
                                @NotBlank String profileImgUrl,
                                @NotBlank String introduction
) {
}