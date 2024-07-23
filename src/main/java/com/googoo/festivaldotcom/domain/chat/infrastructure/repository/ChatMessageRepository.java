package com.googoo.festivaldotcom.domain.chat.infrastructure.repository;

import com.googoo.festivaldotcom.domain.chat.domain.model.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    List<ChatMessage> findByRoomIdOrderBySentAtAsc(String roomId);


}
