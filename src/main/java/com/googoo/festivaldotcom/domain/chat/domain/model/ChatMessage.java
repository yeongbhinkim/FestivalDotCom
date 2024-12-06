package com.googoo.festivaldotcom.domain.chat.domain.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Builder
@Document(collection = "chat_messages")
public class ChatMessage {

    @Id
    private String mongoId; // MongoDB의 기본 식별자 (자동 생성됨)

    private Long id; // 사용자 ID
    private Long roomId; // 채팅방 ID
    private String senderId; // 발신자 ID
    private String content; // 메시지 내용
    private Date sentAt; // 전송 시간
    private String typeMessages; // 메시지 타입
}