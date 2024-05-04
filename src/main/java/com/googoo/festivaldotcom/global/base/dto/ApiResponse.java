package com.googoo.festivaldotcom.global.base.dto;

public record ApiResponse<T> (
        T data
){

}