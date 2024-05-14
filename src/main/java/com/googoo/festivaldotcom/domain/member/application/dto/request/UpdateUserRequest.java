package com.googoo.festivaldotcom.domain.member.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public record UpdateUserRequest(@NotBlank String nickName,
                                String profileImgUrl,
                                @NotBlank String introduction,
                                MultipartFile file,
                                BigDecimal mannerScore
) {
}