package com.googoo.festivaldotcom.domain.festivalApi.service;

import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatgptService chatGptService;

    public String getChatResponse(String prompt) {
        return chatGptService.sendMessage(prompt);
    }

    public String getImageResponse(String prompt) {
        return chatGptService.imageGenerate(prompt);
    }
}
