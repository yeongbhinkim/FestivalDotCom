package com.googoo.festivaldotcom.function.member.domain.exception;


import com.googoo.festivaldotcom.global.base.exception.ErrorCode;
import com.googoo.festivaldotcom.global.base.exception.ServiceException;

public class UserNotFoundException extends ServiceException {

	private static final ErrorCode ERROR_CODE = ErrorCode.USER_NOT_FOUND;
	private static final String MESSAGE_KEY = "exception.user.notfound";

	public UserNotFoundException(Long userId) {
		super(ERROR_CODE, MESSAGE_KEY, new Object[] {userId});
	}

}

