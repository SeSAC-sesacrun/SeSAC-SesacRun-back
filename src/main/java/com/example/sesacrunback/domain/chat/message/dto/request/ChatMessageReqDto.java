package com.example.sesacrunback.domain.chat.message.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChatMessageReqDto {

    private final Long roomId;
    private final String content;
}
