package com.googoo.festivaldotcom.global.auth.token.exception;


import com.googoo.festivaldotcom.global.base.exception.ErrorCode;
import com.googoo.festivaldotcom.global.base.exception.ServiceException;

public abstract class TokenException extends ServiceException {

    protected TokenException(ErrorCode errorCode, String messageKey) {
        super(errorCode, messageKey, null);
    }
}
