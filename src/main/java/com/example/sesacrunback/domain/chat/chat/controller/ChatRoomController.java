package com.example.sesacrunback.domain.chat.chat.controller;

import com.example.sesacrunback.domain.chat.chat.dto.response.ChatRoomResDto;
import com.example.sesacrunback.domain.chat.chat.service.ChatService;
import com.example.sesacrunback.global.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recruitments/posts/{postId}/chat")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<ApiResponse<ChatRoomResDto>> createOrGetChatRoom(
        @PathVariable Long postId) {

        return ResponseEntity.ok(
            ApiResponse.success(chatService.createOrGetChatRoom(postId, 3L)));
    }

}
