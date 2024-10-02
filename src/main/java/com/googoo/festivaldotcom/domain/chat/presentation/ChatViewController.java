package com.googoo.festivaldotcom.domain.chat.presentation;

import com.googoo.festivaldotcom.domain.chat.domain.model.ChatMessage;
import com.googoo.festivaldotcom.domain.chat.domain.service.ChatRoomService;
import com.googoo.festivaldotcom.domain.chat.domain.service.ChatService;
import com.googoo.festivaldotcom.global.auth.token.service.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatViewController {

    private final JwtTokenProvider jwtTokenProvider;

    private final ChatService chatService;
    private final ChatRoomService chatRoomService;

    private final SimpMessagingTemplate messagingTemplate;
    @GetMapping("/test")
    public String test() {
        return "/chat/main";
    }


    @MessageMapping("/chat.sendMessage")
    public void sendMessage(ChatMessage message) {
        ChatMessage chatMessage = ChatMessage.builder()
                .roomId(message.getRoomId())
                .senderId(message.getSenderId())
                .content(message.getContent())
                .sentAt(new Date())
                .build();

        chatService.saveMessage(chatMessage);
        chatRoomService.updateLastMessageTime(chatMessage.getRoomId());
        messagingTemplate.convertAndSend("/topic/" + chatMessage.getRoomId(), chatMessage);
        messagingTemplate.convertAndSend("/topic/chatRooms", "updated");
    }


    @GetMapping("/chatRooms/{chatRoomId}/messages")
    public List<ChatMessage> getAllMessages(@PathVariable Long chatRoomId) {
        return chatService.getMessagesByChatroomId(chatRoomId);
    }

    @GetMapping("/chatRoom/{chatRoomId}")
    public String getChatRoom(HttpServletRequest request,ModelAndView modelAndView,@PathVariable Long chatRoomId){

        String userToken = jwtTokenProvider.extractTokenFromRequestCookie(request);

        Claims claims = jwtTokenProvider.getClaims(userToken);

        String userId = String.valueOf(claims.get("userId", Long.class));

        modelAndView.addObject("sessionId", userId);
        modelAndView.addObject("currentChatRoomId", chatRoomId);

        return "/chat/chatRoom";
    }

}
                                                