package com.example.sesacrunback.domain.chat.participant.repository;

import com.example.sesacrunback.domain.chat.participant.entity.ChatParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {

    boolean existsByChatIdAndUserId(Long chatId, Long userId);
}
