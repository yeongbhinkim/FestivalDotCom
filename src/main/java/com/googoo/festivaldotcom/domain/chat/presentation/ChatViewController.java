package com.googoo.festivaldotcom.domain.chat.presentation;

import com.googoo.festivaldotcom.domain.chat.application.projection.RoomLastMessageProjection;
import com.googoo.festivaldotcom.domain.chat.domain.model.ChatMessage;
import com.googoo.festivaldotcom.domain.chat.domain.model.Rooms;
import com.googoo.festivaldotcom.domain.chat.domain.service.ChatRoomService;
import com.googoo.festivaldotcom.domain.chat.domain.service.ChatService;
import com.googoo.festivaldotcom.global.auth.token.dto.jwt.JwtAuthentication;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/chat/view")
@RequiredArgsConstructor
public class ChatViewController {

    private final ChatService chatService;
    private final ChatRoomService chatRoomService;

    private final SimpMessagingTemplate messagingTemplate;

    @Operation(summary = "채팅 목록", description = "사용자 채팅 목록 / 화면")
    @GetMapping("/chatList")
    public String chatList(
            HttpServletRequest request,
            Model model,
            @Parameter(description = "인증된 사용자 정보") @AuthenticationPrincipal JwtAuthentication user) {

        long chatRoomsByUserIdCount = chatRoomService.getChatRoomsByUserIdCount(user.id());
        if (chatRoomsByUserIdCount == 0) {
            model.addAttribute("messageList", "메시지가 없습니다.");
            model.addAttribute("roomList", Collections.emptyList());
            return "/chat/chatListPage";
        }

        try {
            List<RoomLastMessageProjection> lastMessage = chatService.getLastMessage(user.id());
            List<Rooms> roomsList = chatRoomService.getChatRoomsByUserId(user.id());

            model.addAttribute("messageList", lastMessage);
            model.addAttribute("roomList", roomsList);
        } catch (Exception e) {
            log.error("채팅 목록 조회 중 오류 발생: ", e);
            model.addAttribute("messageList", "채팅 목록을 가져오는 중 오류가 발생했습니다. 다시 시도해주세요.");
            model.addAttribute("roomList", Collections.emptyList());
        }

        return "/chat/chatListPage";
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
        chatRoomService.modifyLastMessageTime(chatMessage.getRoomId());
        messagingTemplate.convertAndSend("/topic/" + chatMessage.getRoomId(), chatMessage);
        messagingTemplate.convertAndSend("/topic/chatRooms", "updated");
    }


    @GetMapping("/chatRooms/{chatRoomId}/messages")
    public List<ChatMessage> getAllMessages(@PathVariable Long chatRoomId) {
        return chatService.getMessagesByChatroomId(chatRoomId);
    }

    @Operation(summary = "채팅방", description = "사용자 채팅방 / 화면")
    @GetMapping("/chatRoom/{chatRoomId}")
    public String getChatRoom(
            HttpServletRequest request,
            ModelAndView modelAndView,
            @PathVariable Long chatRoomId,
            @Parameter(description = "인증된 사용자 정보") @AuthenticationPrincipal JwtAuthentication user) {

        String userId = user.id().toString();

        modelAndView.addObject("sessionId", userId);
        modelAndView.addObject("currentChatRoomId", chatRoomId);

        return "/chat/chatRoomPage";
    }

}
                                                