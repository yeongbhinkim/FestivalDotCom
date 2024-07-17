package com.googoo.festivaldotcom.domain.chat.domain.service;

import com.googoo.festivaldotcom.domain.chat.application.dto.request.RoomMemberDTO;
import com.googoo.festivaldotcom.domain.chat.domain.model.Rooms;
import com.googoo.festivaldotcom.domain.chat.infrastructure.mapper.ChatRoomMapper;
import com.googoo.festivaldotcom.domain.chat.infrastructure.mapper.ChatUserMapper;
import com.googoo.festivaldotcom.domain.festival.domain.model.Festival;
import com.googoo.festivaldotcom.domain.member.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RoomMemberService {

    private final ChatUserMapper chatUserMapper;
    private final ChatRoomMapper chatRoomMapper;

    public List<RoomMemberDTO> getChatUserRoom(Festival festival) {

        List<User> males = chatUserMapper.selectUserGender(festival.getFestivalId(), "male");
        List<User> females = chatUserMapper.selectUserGender(festival.getFestivalId(), "female");

        Collections.shuffle(males);
        Collections.shuffle(females);

        List<Rooms> createdRooms = new ArrayList<>();

        while (males.size() >= 2 && females.size() >= 2) {
            List<Long> selectedUserIds = Arrays.asList(
                    males.remove(males.size() - 1).getId(),
                    males.remove(males.size() - 1).getId(),
                    females.remove(females.size() - 1).getId(),
                    females.remove(females.size() - 1).getId()
            );


            Rooms rooms = Rooms.builder()
                    .festivalId(festival.getFestivalId())
                    .roomName(festival.getFestivalName())
                    .build();

            long roomId = chatRoomMapper.insertRoom(rooms);

            RoomMemberDTO roomMemberDTO = RoomMemberDTO.builder()
                    .roomId(roomId)
                    .userIds(selectedUserIds).build();


            chatRoomMapper.insertRoomMember(roomMemberDTO);
        }

        // 남은 사용자들에 대한 처리
        if (!males.isEmpty() || !females.isEmpty()) {
            String errorMsg = "할당되지 않은 사용자가 있습니다: " +
                    "남성: " + males.size() + "명, " +
                    "여성: " + females.size() + "명";
            throw new IllegalStateException(errorMsg);
        }

        return null;
    }

}
