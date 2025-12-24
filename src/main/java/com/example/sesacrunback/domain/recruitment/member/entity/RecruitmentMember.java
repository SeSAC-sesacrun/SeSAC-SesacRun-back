package com.example.sesacrunback.domain.recruitment.member.entity;

import com.example.sesacrunback.domain.recruitment.post.entity.RecruitmentPost;
import com.example.sesacrunback.domain.user.entity.User;
import com.example.sesacrunback.global.common.entity.BaseTimeEntity;
import com.example.sesacrunback.global.exception.CustomException;
import com.example.sesacrunback.global.exception.ErrorCode;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "recruitment_members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RecruitmentMember extends BaseTimeEntity { //todo 추후 패키지, 엔티티 이름 수정
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

    public static RecruitmentMember organizer(RecruitmentPost post, User user) {
        return RecruitmentMember.builder()
            .role(MemberRole.ORGANIZER)
            .status(MemberStatus.APPROVED) // 모집자는 항상 승인 상태
            .post(post)
            .user(user)
            .build();
    }

    public static RecruitmentMember participant(RecruitmentPost post, User user) {
        return RecruitmentMember.builder()
            .role(MemberRole.PARTICIPANT)
            .status(MemberStatus.PENDING) // 참여자는 대기 상태
            .post(post)
            .user(user)
            .build();
    }

    public void validateCanApply() {
        if (status == MemberStatus.PENDING) {
            throw new CustomException(ErrorCode.ALREADY_APPLIED);
        }

        if (status == MemberStatus.APPROVED) {
            throw new CustomException(ErrorCode.ALREADY_RECRUITED_MEMBER);
        }
    }
}
