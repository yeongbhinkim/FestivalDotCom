package com.googoo.festivaldotcom.global.utils;

import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.regex.Pattern;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class ValidateUtil {

	public static void checkText(String text, String message) {
		Assert.hasText(text, message);
	}

	public static void checkOverLength(String text, int length, String message) {
		if (Objects.nonNull(text) && text.length() > length) {
			throw new IllegalArgumentException(message);
		}
	}

	private static final Pattern EMAIL_PATTERN = Pattern.compile(
			"^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z]{2,6}$",
			Pattern.CASE_INSENSITIVE
	);

	public static void checkEmail(String email, String errorMessage) {
		if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
			throw new IllegalArgumentException(errorMessage);
		}
	}

}
