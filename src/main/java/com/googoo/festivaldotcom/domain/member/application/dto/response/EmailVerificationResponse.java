package com.googoo.festivaldotcom.domain.member.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailVerificationResponse {

    @Schema(description = "인증 성공 여부", example = "true")
    private boolean success;

    @Schema(description = "응답 메시지", example = "이메일 인증 링크가 전송되었습니다.")
    private String message;
}
