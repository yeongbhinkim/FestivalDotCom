package com.googoo.festivaldotcom.domain.chat.infrastructure.repository;

import com.googoo.festivaldotcom.domain.chat.application.projection.RoomLastMessage;
import com.googoo.festivaldotcom.domain.chat.domain.model.ChatMessage;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    List<ChatMessage> findByRoomIdOrderBySentAtAsc(Long roomId);


    @Aggregation(pipeline = {
            "{ '$match': { 'id': ?0 } }",
            "{ '$sort': { 'roomId': 1, 'sentAt': -1 } }",
            "{ '$group': { '_id': '$roomId', 'lastMessage': { '$first': '$$ROOT' } } }",
            "{ '$project': { " +
                    "   'roomId': { '$ifNull': ['$_id', null] }, " +
                    "   'lastMessage': { '$ifNull': ['$lastMessage.content', 'No messages yet'] }, " +
                    "   'typeMessages': { '$ifNull': ['$lastMessage.typeMessages', 'UNKNOWN'] } " +
                    "} }"
    })
    List<RoomLastMessage> findLastMessagesByUserId(Long userId);



}
