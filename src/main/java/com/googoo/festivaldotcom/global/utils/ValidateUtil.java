package com.googoo.festivaldotcom.global.utils;

import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.util.Objects;

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
}
