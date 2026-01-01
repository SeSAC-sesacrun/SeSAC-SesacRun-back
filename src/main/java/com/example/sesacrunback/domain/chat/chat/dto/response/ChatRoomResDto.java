package com.example.sesacrunback.domain.chat.chat.dto.response;

import com.example.sesacrunback.domain.chat.chat.entity.Chat;
import com.example.sesacrunback.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChatRoomResDto {

    private final Long roomId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Long postId;
    private final String opponentName;
    private final Long opponentId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String lastMessage;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final LocalDateTime lastMessageTime;

    public static ChatRoomResDto roomDetail(Chat chat, Long postId, User opponent) {
        return new ChatRoomResDto(
            chat.getId(),
            postId,
            opponent.getName(),
            opponent.getId(),
            null,
            null
        );
    }

    public static ChatRoomResDto roomList(Chat chat, User opponent) {
        return new ChatRoomResDto(
            chat.getId(),
            null,
            opponent.getName(),
            opponent.getId(),
            chat.getLastMessageContent(),
            chat.getUpdatedAt()
        );
    }
}
