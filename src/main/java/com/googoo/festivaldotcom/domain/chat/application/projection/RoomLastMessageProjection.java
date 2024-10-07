package com.googoo.festivaldotcom.domain.chat.application.projection;

public interface RoomLastMessageProjection {

    Long getRoomId();            // 각 채팅방의 ID
    String getLastMessage();     // 각 채팅방의 가장 최근 메시지

}
