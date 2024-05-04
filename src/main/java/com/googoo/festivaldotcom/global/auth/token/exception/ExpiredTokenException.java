package com.googoo.festivaldotcom.global.auth.token.exception;


import com.googoo.festivaldotcom.global.base.exception.ErrorCode;

public class ExpiredTokenException extends TokenException {

    private static final ErrorCode ERROR_CODE = ErrorCode.EXPIRED_TOKEN;
    private static final String MESSAGE_KEY = "exception.token.expired";

    public ExpiredTokenException() {
        super(ERROR_CODE, MESSAGE_KEY);
    }

}
