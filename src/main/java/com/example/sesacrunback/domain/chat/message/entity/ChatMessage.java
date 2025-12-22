package com.example.sesacrunback.domain.chat.message.entity;

import com.example.sesacrunback.domain.chat.chat.entity.Chat;
import com.example.sesacrunback.domain.user.entity.User;
import com.example.sesacrunback.global.common.entity.BaseCreateEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "chat_messages")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage extends BaseCreateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

    @Column(nullable = false)
    private String content; // 메시지 내용

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageType type; // 메시지 타입 (일반, 시스템)

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat; // 소속된 채팅방

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender; // 발신자
}
