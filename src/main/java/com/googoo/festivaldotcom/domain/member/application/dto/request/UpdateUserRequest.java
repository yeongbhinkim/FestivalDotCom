package com.googoo.festivaldotcom.domain.member.application.dto.request;

import com.googoo.festivaldotcom.domain.member.application.dto.response.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public record UpdateUserRequest(

        @Schema(description = "사용자의 닉네임", example = "johnDoe123")
        @NotBlank(message = "닉네임은 필수 입력 항목입니다.")  // 유효성 검사 메시지
        String nickName,

        @Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.jpg")
        String profileImgUrl,

        @Schema(description = "자기소개", example = "저는 축제를 좋아하는 사람입니다.")
        @NotBlank(message = "자기소개는 필수 입력 항목입니다.")
        String introduction,

        @Schema(description = "업로드할 프로필 이미지 파일")
        MultipartFile file,

        @Schema(description = "매너 점수", example = "36.5")
        BigDecimal mannerScore,

        @Schema(description = "사용자의 성별", example = "MALE")
        Gender gender,

        @Schema(description = "회사 이메일 주소", example = "user@company.com")
        String companyEmail
) {
}
