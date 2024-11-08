package com.googoo.festivaldotcom.domain.member.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import lombok.Data;

@Data
public class OutForm {

    @Schema(description = "탈퇴 안내 사항에 동의 여부", example = "true")
    @AssertTrue(message = "탈퇴 안내 사항에 동의해야 합니다.")
    private boolean agree;

}
