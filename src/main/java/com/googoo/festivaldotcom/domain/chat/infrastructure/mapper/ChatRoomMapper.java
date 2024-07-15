package com.googoo.festivaldotcom.domain.chat.infrastructure.mapper;

import com.googoo.festivaldotcom.domain.chat.application.dto.request.RegisDTO;
import com.googoo.festivaldotcom.domain.chat.application.dto.request.RoomMemberDTO;
import com.googoo.festivaldotcom.domain.chat.domain.model.Rooms;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ChatRoomMapper {

    int insertRoom(Rooms room);
    void insertRoomMember(RoomMemberDTO roomMemberDTO);

    void insertRegistration(RegisDTO regisDTO);


}