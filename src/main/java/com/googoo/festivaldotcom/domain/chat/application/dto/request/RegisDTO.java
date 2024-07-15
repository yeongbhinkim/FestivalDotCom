package com.googoo.festivaldotcom.domain.chat.application.dto.request;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder(access = lombok.AccessLevel.PUBLIC)
public class RegisDTO {
    private final Long registration_id;
    private final Long festival_id;
    private final Long id; //userId
}