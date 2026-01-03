package com.example.sesacrunback.domain.chat.message.repository;

import com.example.sesacrunback.domain.chat.message.entity.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    Slice<ChatMessage> findAllByChatId(Long chatId, Pageable pageable);

}
