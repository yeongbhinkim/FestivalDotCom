package com.googoo.festivaldotcom.global.auth.token.exception;


import com.googoo.festivaldotcom.global.base.exception.ErrorCode;
import com.googoo.festivaldotcom.global.base.exception.ServiceException;

public class NotFoundCookieException extends ServiceException {

    private static final ErrorCode ERROR_CODE = ErrorCode.NOT_FOUND_COOKIE;
    private static final String MESSAGE_KEY = "exception.user.notfound";

    public NotFoundCookieException() {
        super(ERROR_CODE, MESSAGE_KEY, new String[] {});
    }
}
