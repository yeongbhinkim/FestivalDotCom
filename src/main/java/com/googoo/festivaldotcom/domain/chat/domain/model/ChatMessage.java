package com.googoo.festivaldotcom.domain.chat.domain.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Builder(access = lombok.AccessLevel.PUBLIC)
@Document(collection = "chat_messages")
public class ChatMessage {

    private final String id;
    private final String roomId;
    private final Long senderId;
    private final String content;
    private final Date timestamp;
    private final MessageType type;

    public enum MessageType {
        SENT, RECEIVED
    }
}