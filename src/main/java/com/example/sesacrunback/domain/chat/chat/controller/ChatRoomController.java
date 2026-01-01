package com.example.sesacrunback.domain.chat.chat.controller;

import com.example.sesacrunback.domain.chat.chat.dto.response.ChatRoomResDto;
import com.example.sesacrunback.domain.chat.chat.service.ChatService;
import com.example.sesacrunback.global.common.dto.ApiResponse;
import com.example.sesacrunback.global.security.CustomUserDetails;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatService chatService;

    @PostMapping("/recruitments/posts/{postId}/chat")
    public ResponseEntity<ApiResponse<ChatRoomResDto>> createOrGetChatRoom(
        @PathVariable Long postId, @AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity.ok(
            ApiResponse.success(chatService.createOrGetChatRoom(postId, userDetails.getId())));
    }

    @GetMapping("/chatrooms")
    public ResponseEntity<ApiResponse<List<ChatRoomResDto>>> getChatRooms(
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(
            ApiResponse.success(chatService.getChatRooms(userDetails.getId())));
    }

}
