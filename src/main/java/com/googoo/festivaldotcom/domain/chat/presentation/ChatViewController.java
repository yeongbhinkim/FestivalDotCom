package com.googoo.festivaldotcom.domain.chat.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chat")
public class ChatViewController {

    @GetMapping("/test")
    public String test() {
        return "/chat/main";
    }
}
