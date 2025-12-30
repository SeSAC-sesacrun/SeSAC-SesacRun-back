package com.example.sesacrunback.domain.chat.message.service;

import com.example.sesacrunback.domain.chat.chat.entity.Chat;
import com.example.sesacrunback.domain.chat.chat.repository.ChatRepository;
import com.example.sesacrunback.domain.chat.message.dto.request.ChatMessageReqDto;
import com.example.sesacrunback.domain.chat.message.dto.response.ChatMessageResDto;
import com.example.sesacrunback.domain.chat.message.entity.ChatMessage;
import com.example.sesacrunback.domain.chat.message.repository.ChatMessageRepository;
import com.example.sesacrunback.domain.chat.participant.repository.ChatParticipantRepository;
import com.example.sesacrunback.domain.user.entity.User;
import com.example.sesacrunback.domain.user.repository.UserRepository;
import com.example.sesacrunback.global.exception.CustomException;
import com.example.sesacrunback.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatMessageService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public ChatMessageResDto saveMessage(ChatMessageReqDto reqDto, Long senderId) {
        Chat chat = chatRepository.findById(reqDto.getRoomId())
            .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        User sender = userRepository.findById(senderId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 채팅 참여자인지
        if (!chatParticipantRepository.existsByChatIdAndUserId(chat.getId(), senderId)) {
            throw new CustomException(ErrorCode.CHAT_NOT_PARTICIPANT);
        }

        ChatMessage chatMessage = ChatMessage.of(reqDto.getContent(), chat, sender);

        return ChatMessageResDto.from(chatMessageRepository.save(chatMessage));

    }
}
