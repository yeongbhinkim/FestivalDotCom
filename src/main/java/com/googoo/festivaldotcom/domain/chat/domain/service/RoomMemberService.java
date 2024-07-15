package com.googoo.festivaldotcom.domain.chat.domain.service;

import com.googoo.festivaldotcom.domain.chat.application.dto.request.RoomMemberDTO;
import com.googoo.festivaldotcom.domain.chat.domain.model.Rooms;
import com.googoo.festivaldotcom.domain.chat.infrastructure.mapper.ChatRoomMapper;
import com.googoo.festivaldotcom.domain.chat.infrastructure.mapper.ChatUserMapper;
import com.googoo.festivaldotcom.domain.festival.domain.model.Festival;
import com.googoo.festivaldotcom.domain.member.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoomMemberService {

    private final ChatUserMapper chatUserMapper;
    private final ChatRoomMapper chatRoomMapper;

    public List<RoomMemberDTO> getChatUserRoom(RoomMemberDTO roomMemberDTO, Festival festival) {

        List<User> males = chatUserMapper.selectUserGender(festival.getFestivalId(), "male");
        List<User> females = chatUserMapper.selectUserGender(festival.getFestivalId(), "female");

        // 이미 채팅방에 할당된 사용자 제외

        Collections.shuffle(males);
        Collections.shuffle(females);

        List<Rooms> createdRooms = new ArrayList<>();

        while (males.size() >= 2 && females.size() >= 2) {
            List<Long> selectedUserIds = new ArrayList<>();
            selectedUserIds.add(males.remove(males.size() - 1).getId());
            selectedUserIds.add(males.remove(males.size() - 1).getId());
            selectedUserIds.add(females.remove(females.size() - 1).getId());
            selectedUserIds.add(females.remove(females.size() - 1).getId());


            Rooms rooms = Rooms.builder()
                    .festivalId(festival.getFestivalId())
                    .roomName(festival.getFestivalName())
                    .build();

            int roomId = chatRoomMapper.insertRoom(rooms);



//            chatRoom.setFestivalId(festivalId);
//            chatRoom.setUserIds(selectedUserIds);
//
//            chatRoomMapper.createChatRoom(chatRoom);
//            createdRooms.add(chatRoom);
        }

//        // 남은 사용자들에 대한 처리
//        if (males.size() >= 2 || females.size() >= 2) {
//            throw new InsufficientUsersException("채팅방 생성 후 남은 사용자가 있습니다: 남성 " + males.size() + "명, 여성 " + females.size() + "명");
//        }

        return null;
    }

}
