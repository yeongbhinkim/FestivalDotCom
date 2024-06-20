package com.googoo.festivaldotcom.domain.chat.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
//@Setter
@Builder
public class Rooms {
    private final Long roomId;
    private final Long festivalId;
    private final String roomName;
    private final LocalDate createdAt;
    @Setter
    private boolean deleted;

}