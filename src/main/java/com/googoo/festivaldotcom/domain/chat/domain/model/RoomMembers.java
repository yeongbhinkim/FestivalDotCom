package com.googoo.festivaldotcom.domain.chat.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Builder(access = lombok.AccessLevel.PUBLIC)
public class RoomMembers {

    private final Long roomId;
    private final Long id;
    private final LocalDate createdAt;
    @Setter
    private boolean deleted;
}