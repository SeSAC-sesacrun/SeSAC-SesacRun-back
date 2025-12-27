package com.example.sesacrunback.domain.chat.participant.entity;

import com.example.sesacrunback.domain.chat.chat.entity.Chat;
import com.example.sesacrunback.domain.user.entity.User;
import com.example.sesacrunback.global.common.entity.BaseCreateEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "chat_participants")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatParticipant extends BaseCreateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private ChatRole role; // HOST, GUEST

    private LocalDateTime lastReadAt; // 읽음 처리용

}
