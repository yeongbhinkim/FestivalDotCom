package com.googoo.festivaldotcom.global.base.dto;

import com.googoo.festivaldotcom.global.base.exception.ErrorCode;

public record ErrorResponse(
        String code,
        String message
) {

    public static ErrorResponse of(ErrorCode code) {
        return new ErrorResponse(code.getCode(), code.getMessage());
    }

}
