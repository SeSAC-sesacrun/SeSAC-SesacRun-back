package com.example.sesacrunback.domain.recruitment.member.entity;

import com.example.sesacrunback.domain.recruitment.post.entity.RecruitmentPost;
import com.example.sesacrunback.domain.user.entity.User;
import com.example.sesacrunback.global.common.entity.BaseTimeEntity;
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
@Table(name = "recruitment_members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitmentMember extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole role; // 역할 (모집자, 참여자)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus status; // 상태 (대기중, 승인됨, 거절됨)

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private RecruitmentPost post; // 참여한 모집글

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 참여한 사용자
}
