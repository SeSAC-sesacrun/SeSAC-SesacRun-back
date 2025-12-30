package com.example.sesacrunback.domain.chat.message.dto.response;

import com.example.sesacrunback.domain.chat.message.entity.ChatMessage;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChatMessageResDto {

    private final Long id;
    private final Long roomId;
    private final Long senderId;
    private final String senderName;
    private final String content;
    private final LocalDateTime sendTime;

    public static ChatMessageResDto from(ChatMessage message) {
        return new ChatMessageResDto(message.getId(), message.getChat().getId(),
            message.getSender().getId(), message.getSender()
            .getName(), message.getContent(), message.getCreatedAt());
    }

}
