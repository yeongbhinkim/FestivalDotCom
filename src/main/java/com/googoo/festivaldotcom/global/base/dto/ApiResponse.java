package com.googoo.festivaldotcom.global.base.dto;

public record ApiResponse<T>(
        boolean success,
        String message,
        T data
) {

}