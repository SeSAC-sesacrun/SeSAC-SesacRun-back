package com.example.sesacrunback.domain.chat.participant.repository;

import com.example.sesacrunback.domain.chat.chat.entity.Chat;
import com.example.sesacrunback.domain.chat.participant.entity.ChatParticipant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {

    boolean existsByChatIdAndUserId(Long chatId, Long userId);

    @Query("""
            select c.chat
            from ChatParticipant c
            where c.user.id = :userId
            
           """)
    List<Chat> findChatRoomsByUserId(@Param("userId") Long userId);

    @Query("""
            select distinct c
            from Chat c
            join fetch c.participants cp
            join fetch cp.user u
            join ChatParticipant  myCp on myCp.chat = c
            where myCp.user.id = :userId
           """)
    List<Chat> findChatsWithAllParticipants(@Param("userId") Long userId);
}
