package com.googoo.festivaldotcom.domain.chat.application.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Builder
public class RoomMemberDTO {

    private final Long roomId;
    private final List<Long> userIds;

}