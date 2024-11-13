package com.googoo.festivaldotcom.domain.chat.domain.service;

import com.googoo.festivaldotcom.domain.chat.application.dto.request.RoomMemberDTO;
import com.googoo.festivaldotcom.domain.chat.domain.model.Rooms;
import com.googoo.festivaldotcom.domain.chat.infrastructure.mapper.ChatRoomMapper;
import com.googoo.festivaldotcom.domain.chat.infrastructure.mapper.ChatUserMapper;
import com.googoo.festivaldotcom.domain.festival.domain.model.Festival;
import com.googoo.festivaldotcom.domain.member.domain.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomMemberService {

    private final ChatUserMapper chatUserMapper;
    private final ChatRoomMapper chatRoomMapper;


    @Transactional
    public void getChatUserRoom(Festival festival) {

        List<User> males = chatUserMapper.selectUserGender(festival.getFestivalId(), "male");
        List<User> females = chatUserMapper.selectUserGender(festival.getFestivalId(), "female");

        Collections.shuffle(males);
        Collections.shuffle(females);

        try {
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
                if (roomId <= 0) {
                    throw new RuntimeException("방 생성에 실패했습니다.");

                }

                RoomMemberDTO roomMemberDTO = RoomMemberDTO.builder()
                        .roomId(roomId)
                        .userIds(selectedUserIds).build();

                long rowsInserted = chatRoomMapper.insertRoomMember(roomMemberDTO);
                if (rowsInserted != selectedUserIds.size()) {
                    throw new RuntimeException("모든 사용자들을 방에 추가하는 데 실패했습니다.");
                }
            }
        } catch (DataAccessException e) {
            throw new RuntimeException("데이터베이스 접근 중 오류가 발생했습니다.", e);
        } catch (RuntimeException e) {
            throw new IllegalStateException("방 생성에 실패했습니다.", e);
        }

        // 남은 사용자들에 대한 처리
        if (!males.isEmpty() || !females.isEmpty()) {
            String errorMsg = "할당되지 않은 사용자가 있습니다: " +
                    "남성: " + males.size() + "명, " +
                    "여성: " + females.size() + "명";
            throw new IllegalStateException(errorMsg);
        }
    }

}
