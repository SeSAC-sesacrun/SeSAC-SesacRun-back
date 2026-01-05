package com.example.sesacrunback.domain.chat.message.controller;

import com.example.sesacrunback.domain.chat.message.dto.request.ChatMessageReqDto;
import com.example.sesacrunback.domain.chat.message.service.ChatMessageService;
import com.example.sesacrunback.global.security.CustomUserDetails;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final SimpMessageSendingOperations messageSendingOperations;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat/message")
    public void sendMessage(@Payload ChatMessageReqDto reqDto, SimpMessageHeaderAccessor accessor) {
        messageSendingOperations.convertAndSend("/sub/chat/room/" + reqDto.getRoomId(),
            chatMessageService.saveMessage(reqDto, getUserId(accessor.getUser())));

    }

    private Long getUserId(Principal principal) {
        return ((CustomUserDetails) ((UsernamePasswordAuthenticationToken) principal).getPrincipal()).getId();
    }

}
