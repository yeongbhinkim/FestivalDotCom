package com.googoo.festivaldotcom.domain.chat.domain.service;

import com.googoo.festivaldotcom.domain.chat.domain.model.Rooms;
import com.googoo.festivaldotcom.domain.chat.infrastructure.mapper.ChatRoomMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomMapper chatRoomMapper;


    public void modifyLastMessageTime(Long roomId) {
        chatRoomMapper.updateLastMessageTime(roomId);
    }

    public List<Rooms> getChatRoomsByUserId(Long userId) {
        return chatRoomMapper.selectChatRoomsByUserId(userId);
    }

    public long getChatRoomsByUserIdCount(Long userId) {
        return chatRoomMapper.selectChatRoomsByUserIdCount(userId);
    }


    public List<Long> getRoomsByUserId(Long userId) {
        return chatRoomMapper.selectRoomsByUserId(userId);
    }
}
