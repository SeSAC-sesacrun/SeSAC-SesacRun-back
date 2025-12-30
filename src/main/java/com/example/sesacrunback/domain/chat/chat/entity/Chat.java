package com.example.sesacrunback.domain.chat.chat.entity;

import com.example.sesacrunback.domain.chat.participant.entity.ChatParticipant;
import com.example.sesacrunback.domain.chat.participant.entity.ChatRole;
import com.example.sesacrunback.domain.recruitment.post.entity.RecruitmentPost;
import com.example.sesacrunback.domain.user.entity.User;
import com.example.sesacrunback.global.common.entity.BaseCreateEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "chat")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat extends BaseCreateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private RecruitmentPost post; // 모집글

    @Column(nullable = false)
    private String name; // 채팅방 이름

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatParticipant> participants = new ArrayList<>();

    private Chat(RecruitmentPost post) {
        this.post = post;
        this.name = post.getTitle();
    }

    private void addParticipant(User user, ChatRole role) {
        ChatParticipant participant = ChatParticipant.of(this, user, role);
        this.participants.add(participant);
    }

    public static Chat of(RecruitmentPost post, User host, User member) {
        Chat chat = new Chat(post);

        chat.addParticipant(host, ChatRole.HOST);
        chat.addParticipant(member, ChatRole.MEMBER);
        return chat;
    }
}
