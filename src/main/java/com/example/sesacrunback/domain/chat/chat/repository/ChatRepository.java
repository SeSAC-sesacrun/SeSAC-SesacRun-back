package com.example.sesacrunback.domain.chat.chat.repository;

import com.example.sesacrunback.domain.chat.chat.entity.Chat;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("""
         select c from Chat c
         join c.participants p1
         join c.participants p2
         where c.post.id = :postId
         and p1.user.id = :userId
         and p2.user.id = :targetUserId
        """)
    Optional<Chat> findExistingChat(@Param("postId") Long postId, @Param("userId") Long userId,
        @Param("targetUserId") Long targetUserId);
}
