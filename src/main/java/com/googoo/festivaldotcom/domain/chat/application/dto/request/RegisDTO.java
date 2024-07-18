package com.googoo.festivaldotcom.domain.chat.application.dto.request;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class RegisDTO {
    private final Long festival_id;
    private final Long id; //userId
}