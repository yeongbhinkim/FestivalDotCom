package com.googoo.festivaldotcom.domain.chat.domain.service;

import com.googoo.festivaldotcom.domain.chat.domain.model.ChatMessage;
import com.googoo.festivaldotcom.domain.chat.domain.model.RoomMembers;
import com.googoo.festivaldotcom.domain.chat.domain.model.Rooms;
import com.googoo.festivaldotcom.domain.chat.infrastructure.mapper.ChatRoomMapper;
import com.googoo.festivaldotcom.domain.chat.infrastructure.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomMapper chatRoomMapper;
    private final ChatMessageRepository chatMessageRepository;

    public ChatMessage saveMessage(ChatMessage chatMessage) {
        return chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessage> getMessagesByChatroomId(Long chatroomId) {
        return chatMessageRepository.findByRoomIdOrderBySentAtAsc(String.valueOf(chatroomId));
    }

//    public List<RoomMembers> getUserChatRooms(String userId) {
//        return chatRoomMapper.(userId);
//    }

//    public Rooms getChatRoomById(Long chatroomId) {
//        return chatRoomMapper.findById(chatroomId)
//                .orElseThrow(() -> new RuntimeException("Chat room not found: " + chatroomId));
//    }


    }