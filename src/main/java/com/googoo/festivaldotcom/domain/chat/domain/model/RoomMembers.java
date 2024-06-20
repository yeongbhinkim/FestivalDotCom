package com.googoo.festivaldotcom.domain.chat.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Builder
public class RoomMembers {

    private Long roomId;
    private Long id;
    private LocalDate createdAt;
    @Setter
    private boolean deleted;
}