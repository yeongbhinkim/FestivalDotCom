package com.googoo.festivaldotcom.domain.chat.infrastructure.mapper;

import com.googoo.festivaldotcom.domain.chat.application.dto.request.RegisDTO;
import com.googoo.festivaldotcom.domain.chat.application.dto.request.RoomMemberDTO;
import com.googoo.festivaldotcom.domain.chat.domain.model.Rooms;
import com.googoo.festivaldotcom.domain.chat.application.dto.request.SchedulerRoomDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatRoomMapper {


    List<Rooms> selectChatRoomsByUserId(Long userId);
    long selectChatRoomsByUserIdCount(Long userId);
    long insertRoom(Rooms room);
    long insertRoomMember(RoomMemberDTO roomMemberDTO);
    long insertRegistration(RegisDTO regisDTO);
    long selectRegistrationCount(RegisDTO regisDTO);
    void updateLastMessageTime(Long roomId);

    List<SchedulerRoomDTO> findRegistrationByFestivalId(SchedulerRoomDTO schedulerRoomDTO);
    void insertChatRoom(SchedulerRoomDTO schedulerRoomDTO);
    void insertChatMember(SchedulerRoomDTO schedulerRoomDTO);
    void deleteChatMember(SchedulerRoomDTO schedulerRoomDTO);

}
