package com.googoo.festivaldotcom.domain.member.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailVerificationRequest {

    @Schema(description = "사용자의 이메일 주소", example = "user@example.com")
    @Email(message = "유효한 이메일 주소여야 합니다.")
    private String email;

}
