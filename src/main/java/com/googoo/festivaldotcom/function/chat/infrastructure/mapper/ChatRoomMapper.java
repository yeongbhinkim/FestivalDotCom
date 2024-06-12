package com.googoo.festivaldotcom.function.chat.infrastructure.mapper;

import com.googoo.festivaldotcom.function.chat.domain.model.Rooms;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ChatRoomMapper {

    void insertRoom(Rooms room);
    void insertRoomMember(@Param("roomId") Long roomId, @Param("userId") Long userId);
}
