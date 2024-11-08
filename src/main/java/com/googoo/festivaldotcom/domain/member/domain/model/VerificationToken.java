package com.googoo.festivaldotcom.domain.member.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class VerificationToken {

    @Schema(description = "고유한 토큰 ID", example = "1", required = true)
    private Long id;

    @Schema(description = "사용자의 고유 ID", example = "1", required = true)
    private String userId;

    @Schema(description = "사용자의 이메일 주소", example = "user@example.com", required = true)
    private String email;

    @Schema(description = "이메일 인증을 위한 고유한 토큰", example = "a2b3c4d5e6f7g8h9i0j", required = true)
    private String token;

    @Schema(description = "토큰이 생성된 시간", example = "2024-01-01T12:00:00", required = true)
    private LocalDateTime createdAt;

    public VerificationToken(String email, String token, String userId) {
        this.userId = userId;
        this.email = email;
        this.token = token;
        this.createdAt = LocalDateTime.now();
    }
}
