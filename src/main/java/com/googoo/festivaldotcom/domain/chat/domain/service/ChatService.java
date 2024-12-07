package com.googoo.festivaldotcom.domain.chat.domain.service;

import com.googoo.festivaldotcom.domain.chat.application.projection.RoomLastMessage;
import com.googoo.festivaldotcom.domain.chat.domain.model.ChatMessage;
import com.googoo.festivaldotcom.domain.chat.infrastructure.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;

    //메세지 저장
    public void saveMessage(ChatMessage chatMessage) {
        chatMessageRepository.save(chatMessage);
    }

    // 방의 메세지를 불러옴
    public List<ChatMessage> getMessagesByChatroomId(Long roomId) {
        return chatMessageRepository.findByRoomIdOrderBySentAtAsc(roomId);
    }

    public List<RoomLastMessage> getLastMessage(List<Long> roomIds) {
        List<RoomLastMessage> results = chatMessageRepository.findLastMessagesByRoomIds(roomIds);
        if (results == null || results.isEmpty()) {
            log.info("No chat messages found for roomIds: {}", roomIds);
            return Collections.emptyList(); // 빈 리스트 반환
        }
        return results;
    }

    }