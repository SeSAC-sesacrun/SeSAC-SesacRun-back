package com.example.sesacrunback.domain.chat.message.controller;

import com.example.sesacrunback.domain.chat.message.dto.request.ChatMessageReqDto;
import com.example.sesacrunback.domain.chat.message.service.ChatMessageService;
import com.example.sesacrunback.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final SimpMessageSendingOperations messageSendingOperations;
    private final ChatMessageService chatMessageService;

    // Todo SimpMessageHeaderAccessor 시큐리티 관련해서 사용 예정
    @MessageMapping("/chat/message")
    public void sendMessage(ChatMessageReqDto reqDto, SimpMessageHeaderAccessor accessor) {
        CustomUserDetails user = (CustomUserDetails) accessor.getUser();
        messageSendingOperations.convertAndSend("/sub/chat/room/" + reqDto.getRoomId(),
            chatMessageService.saveMessage(reqDto, user.getId()));

    }

}
