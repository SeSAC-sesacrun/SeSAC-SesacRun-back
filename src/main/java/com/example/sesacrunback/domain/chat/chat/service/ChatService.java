package com.example.sesacrunback.domain.chat.chat.service;

import com.example.sesacrunback.domain.chat.chat.dto.response.ChatRoomResDto;
import com.example.sesacrunback.domain.chat.chat.entity.Chat;
import com.example.sesacrunback.domain.chat.chat.repository.ChatRepository;
import com.example.sesacrunback.domain.recruitment.post.entity.RecruitmentPost;
import com.example.sesacrunback.domain.recruitment.post.repository.RecruitmentPostRepository;
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
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final RecruitmentPostRepository recruitmentPostRepository;

    @Transactional
    public ChatRoomResDto createOrGetChatRoom(Long postId, Long currentUserId) {

        RecruitmentPost post = recruitmentPostRepository.findById(postId)
            .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        if (post.isPostOwner(currentUserId)) {
            throw new CustomException(ErrorCode.CANNOT_CHAT_WITH_SELF);
        }

        User currentUser = userRepository.findById(currentUserId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        User hostUser = post.getAuthor();

        return chatRepository.findExistingChat(post.getId(), currentUser.getId(), hostUser.getId())
            .map(chat -> ChatRoomResDto.from(chat, post.getId(), hostUser))
            .orElseGet(() -> createChatRoom(post, currentUser, hostUser));
    }

    private ChatRoomResDto createChatRoom(RecruitmentPost post, User currentUser, User hostUser) {
        Chat chat = chatRepository.save(Chat.from(post, hostUser, currentUser));

        return ChatRoomResDto.from(chat, post.getId(), hostUser);
    }
}
