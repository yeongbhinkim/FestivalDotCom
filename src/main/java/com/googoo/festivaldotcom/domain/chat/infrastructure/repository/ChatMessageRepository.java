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
            "{ '$match': { 'roomId': { '$in': ?0 } } }", // 사용자가 참여한 방만 필터링
            "{ '$sort': { 'roomId': 1, 'sentAt': -1 } }", // 모든 메시지 정렬
            "{ '$group': { '_id': '$roomId', 'lastMessage': { '$first': '$$ROOT' } } }", // roomId별 최신 메시지 선택
            "{ '$project': { " +
                    "   'roomId': '$_id', " +
                    "   'lastMessage': '$lastMessage.content', " +
                    "} }"
    })
    List<RoomLastMessage> findLastMessagesByRoomIds(List<Long> roomIds);

}
