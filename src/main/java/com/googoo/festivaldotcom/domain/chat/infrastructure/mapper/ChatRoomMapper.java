package com.googoo.festivaldotcom.domain.chat.infrastructure.mapper;

import com.googoo.festivaldotcom.domain.chat.application.dto.request.RegisDTO;
import com.googoo.festivaldotcom.domain.chat.application.dto.request.RoomMemberDTO;
import com.googoo.festivaldotcom.domain.chat.domain.model.Rooms;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatRoomMapper {


    List<Rooms> selectChatRoomsByUserId(String userId);
    long insertRoom(Rooms room);
    long insertRoomMember(RoomMemberDTO roomMemberDTO);
    long insertRegistration(RegisDTO regisDTO);
    void updateLastMessageTime(Long roomId);
}