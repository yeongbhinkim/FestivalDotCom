package com.googoo.festivaldotcom.domain.festivalApi.presentation;

import com.googoo.festivaldotcom.domain.festivalApi.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/chat-gpt")
public class ChatGptController {
    private final ChatService chatService;

    @PostMapping("")
    public String test(@RequestBody String question) {
        return chatService.getChatResponse(question);
    }

    @PostMapping("/getImageData")
    public String Image(@RequestBody String fullChat) {
        return chatService.getImageResponse(fullChat);
    }
}
