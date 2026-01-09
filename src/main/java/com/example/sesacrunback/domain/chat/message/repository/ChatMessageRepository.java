package com.example.sesacrunback.domain.chat.message.repository;

import com.example.sesacrunback.domain.chat.message.entity.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    Slice<ChatMessage> findAllByChatId(Long chatId, Pageable pageable);

    @Modifying
    @Query("""
            delete from ChatMessage cm
            where cm.chat.id in (
                select c.id from Chat c where c.post.id = :postId
            )
        """)
    void deleteByPostId(@Param("postId") Long postId);

}
