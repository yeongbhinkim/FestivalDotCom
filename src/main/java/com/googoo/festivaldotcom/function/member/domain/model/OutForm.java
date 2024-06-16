package com.googoo.festivaldotcom.function.member.domain.model;

import jakarta.validation.constraints.AssertTrue;
import lombok.Data;

@Data
public class OutForm {

    @AssertTrue(message = "탈퇴 안내 사항에 동의해야 합니다.")
    private boolean agree;

}
