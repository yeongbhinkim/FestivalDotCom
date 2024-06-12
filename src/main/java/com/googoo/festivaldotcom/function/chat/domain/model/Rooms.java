package com.googoo.festivaldotcom.function.chat.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
//@Setter
@Builder
public class Rooms {
    private final Long roomId;
    private final Long festivalId;
    private final String roomName;
    private final LocalDateTime createdAt;
    @Setter
    private boolean deleted;

}
