package com.googoo.festivaldotcom.domain.chat.domain.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Builder
@Document(collection = "chat_messages")
public class ChatMessage {

    private final Long id;
    private final Long roomId;
    private final Long senderId;
    private final String content;
    private final Date sentAt;
    private final MessageType type;

    public enum MessageType {
        SENT, RECEIVED
    }
}