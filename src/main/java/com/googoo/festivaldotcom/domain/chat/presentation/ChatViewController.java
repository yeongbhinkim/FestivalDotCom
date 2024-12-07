package com.googoo.festivaldotcom.domain.chat.presentation;

import com.googoo.festivaldotcom.domain.chat.application.projection.RoomLastMessage;
import com.googoo.festivaldotcom.domain.chat.domain.model.ChatMessage;
import com.googoo.festivaldotcom.domain.chat.domain.model.Rooms;
import com.googoo.festivaldotcom.domain.chat.domain.service.ChatRoomService;
import com.googoo.festivaldotcom.domain.chat.domain.service.ChatService;
import com.googoo.festivaldotcom.global.auth.token.dto.jwt.JwtAuthentication;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/chat/view")
@RequiredArgsConstructor
public class ChatViewController {

    private final ChatService chatService;
    private final ChatRoomService chatRoomService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/chatListView")
    public String chatList(Model model) {
        model.addAttribute("pageTitle", "채팅 목록");
        return "chat/chatListPage";
    }

    @Operation(summary = "채팅방 목록", description = "사용자 채팅방 목록을 JSON으로 반환")
    @ResponseBody
    @GetMapping("/chatList")
    public Map<String, Object> getChatList(@AuthenticationPrincipal JwtAuthentication user) {
        Map<String, Object> response = new HashMap<>();
        try {
            long chatRoomsByUserIdCount = chatRoomService.getChatRoomsByUserIdCount(user.id());
            if (chatRoomsByUserIdCount == 0) {
                response.put("roomList", List.of());
                response.put("messageList", List.of());
                return response;
            }

            List<Long> roomIds = chatRoomService.getRoomsByUserId(user.id());

            List<RoomLastMessage> lastMessages = chatService.getLastMessage(roomIds);
            List<Rooms> roomsList = chatRoomService.getChatRoomsByUserId(user.id());

            response.put("roomList", roomsList);
            response.put("messageList", lastMessages);
        } catch (Exception e) {
            log.error("채팅 목록 조회 중 오류 발생: ", e);
            response.put("roomList", List.of());
            response.put("messageList", List.of());
        }
        return response;
    }

    @MessageMapping("/chat.updateList")
    public void broadcastChatRoomUpdate() {
        messagingTemplate.convertAndSend("/topic/chatList", "updated");
    }


//    @Operation(summary = "채팅 목록", description = "사용자 채팅 목록 / 화면")
//    @GetMapping("/chatList")
//    public String chatList(
//            HttpServletRequest request,
//            Model model,
//            @Parameter(description = "인증된 사용자 정보") @AuthenticationPrincipal JwtAuthentication user) {
//
//        long chatRoomsByUserIdCount = chatRoomService.getChatRoomsByUserIdCount(user.id());
//        if (chatRoomsByUserIdCount == 0) {
//            model.addAttribute("messageList", Collections.emptyList());
//            model.addAttribute("roomList", Collections.emptyList());
//            return "chat/chatListPage";
//        }
//
//        try {
//            List<RoomLastMessage> lastMessage = chatService.getLastMessage(user.id());
//            List<Rooms> roomsList = chatRoomService.getChatRoomsByUserId(user.id());
//
//            model.addAttribute("messageList", lastMessage);
//            model.addAttribute("roomList", roomsList);
//        } catch (Exception e) {
//            log.error("채팅 목록 조회 중 오류 발생: ", e);
//            model.addAttribute("messageList", Collections.emptyList());
//            model.addAttribute("roomList", Collections.emptyList());
//        }
//
//        return "chat/chatListPage";
//    }


    @MessageMapping("/chat.sendMessage")
    public void sendMessage(ChatMessage message) {
        ChatMessage chatMessage = ChatMessage.builder()
                .roomId(message.getRoomId())
                .senderId(message.getSenderId())
                .content(message.getContent())
                .sentAt(new Date())
                .build();

        chatService.saveMessage(chatMessage); // 메시지 저장
        chatRoomService.modifyLastMessageTime(chatMessage.getRoomId()); // 최근 메시지 시간 업데이트

        messagingTemplate.convertAndSend("/topic/" + chatMessage.getRoomId(), chatMessage);
    }

    @GetMapping("/chatRooms/{chatRoomId}/messages")
    @ResponseBody
    public List<ChatMessage> getAllMessages(@PathVariable Long chatRoomId) {
        return chatService.getMessagesByChatroomId(chatRoomId); // 해당 채팅방의 메시지 반환
    }

    @GetMapping("/chatRoom/{roomId}/{roomName}")
    public String getChatRoom(
            @PathVariable Long roomId,
            @PathVariable String roomName,
            @AuthenticationPrincipal JwtAuthentication user,
            Model model) {

        String userId = user.id().toString();
//        log.info("Chat Room ID: {}, Chat Room Name: {} Chat User ID: {}", roomId, roomName, userId);
        // 모델에 데이터 추가
        model.addAttribute("sessionId", userId);
        model.addAttribute("currentChatRoomId", roomId);
        model.addAttribute("roomName", roomName);
        model.addAttribute("messages", chatService.getMessagesByChatroomId(roomId));

        return "chat/chatRoomPage";
    }

}
