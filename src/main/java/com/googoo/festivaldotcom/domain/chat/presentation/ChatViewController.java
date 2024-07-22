package com.googoo.festivaldotcom.domain.chat.presentation;

import com.googoo.festivaldotcom.global.auth.token.service.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatViewController {

    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/test")
    public String test() {
        return "/chat/main";
    }

//    @GetMapping("/chatlist")
//    public String getChatList()
}
