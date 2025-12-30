package com.example.sesacrunback.domain.chat.chat.dto.response;

import com.example.sesacrunback.domain.chat.chat.entity.Chat;
import com.example.sesacrunback.domain.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChatRoomResDto {

    private final Long roomId;
    private final Long postId;
    private final String opponentName;
    private final Long opponentId;

    public static ChatRoomResDto from(Chat chat, Long postId, User opponent) {
        return new ChatRoomResDto(
            chat.getId(),
            postId,
            opponent.getName(),
            opponent.getId()
        );
    }
}
