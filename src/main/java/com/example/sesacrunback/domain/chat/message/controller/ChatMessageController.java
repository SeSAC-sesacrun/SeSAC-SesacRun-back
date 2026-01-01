package com.example.sesacrunback.domain.chat.message.controller;

import com.example.sesacrunback.domain.chat.message.dto.response.ChatMessageResDto;
import com.example.sesacrunback.domain.chat.message.service.ChatMessageService;
import com.example.sesacrunback.global.common.dto.ApiResponse;
import com.example.sesacrunback.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat/rooms")
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    @GetMapping("/{roomId}/messages")
    public ResponseEntity<ApiResponse<Slice<ChatMessageResDto>>> getMessageList(
        @PathVariable Long roomId, @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @AuthenticationPrincipal CustomUserDetails userDetails) {

        Slice<ChatMessageResDto> messages = chatMessageService.getMessages(roomId, page, size,
            userDetails.getId());

        return ResponseEntity.ok(ApiResponse.success(messages));
    }

}
