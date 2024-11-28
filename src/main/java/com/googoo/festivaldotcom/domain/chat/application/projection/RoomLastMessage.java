package com.googoo.festivaldotcom.domain.chat.application.projection;

import lombok.Data;

@Data
public class  RoomLastMessage {
    private String roomId;
    private String lastMessage;
    private String typeMessages;
}
