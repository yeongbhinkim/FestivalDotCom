package com.googoo.festivaldotcom.domain.chat.infrastructure.repository;

import com.googoo.festivaldotcom.domain.chat.application.projection.RoomLastMessageProjection;
import com.googoo.festivaldotcom.domain.chat.domain.model.ChatMessage;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    List<ChatMessage> findByRoomIdOrderBySentAtAsc(Long roomId);


    @Aggregation(pipeline = {
            "{ '$match': { 'id': ?0 } }",  // 유저 ID와 일치하는 메시지들만 필터링
            "{ '$sort': { 'roomId': 1, 'sentAt': -1 } }",  // roomId별로 오름차순 정렬, 동일 roomId 내에서는 sentAt 내림차순 정렬
            "{ '$group': { '_id': '$roomId', 'lastMessage': { '$first': '$$ROOT' } } }",  // 각 방별로 가장 최근 메시지를 그룹화
            "{ '$project': { 'roomId': '$_id', 'lastMessage': '$lastMessage.content' } }"  // 필요한 필드만 반환
    })
    List<RoomLastMessageProjection> findLastMessagesByUserId(Long userId);
}
