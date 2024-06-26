package com.googoo.festivaldotcom.domain.member.application.dto.request;

import com.googoo.festivaldotcom.domain.member.application.dto.response.Gender;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public record UpdateUserRequest(@NotBlank String nickName,
                                String profileImgUrl,
                                @NotBlank String introduction,
                                MultipartFile file,
                                BigDecimal mannerScore,
                                Gender gender,
                                String companyEmail
) {
}