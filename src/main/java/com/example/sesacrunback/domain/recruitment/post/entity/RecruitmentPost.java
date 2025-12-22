package com.example.sesacrunback.domain.recruitment.post.entity;

import com.example.sesacrunback.domain.recruitment.member.entity.RecruitmentMember;
import com.example.sesacrunback.domain.user.entity.User;
import com.example.sesacrunback.global.common.entity.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "recruitment_posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitmentPost extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecruitmentCategory category; // 카테고리 (스터디, 프로젝트)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecruitmentStatus status; // 모집 상태 (모집중, 모집완료)

    @Column(nullable = false)
    private String title; // 제목

    @Column(nullable = false)
    private String content; // 내용

    @Column(nullable = false)
    private Integer currentMembers; // 현재 인원

    @Column(nullable = false)
    private Integer totalMembers; // 총 모집 인원

    @Column(nullable = false)
    private Integer views; // 조회수

    private LocalDateTime deletedAt; // 삭제일시 (Soft Delete)

    @ManyToOne
    @JoinColumn(name = "author_id",nullable = false)
    private User author; // 작성자

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecruitmentMember> recruitmentMembers = new ArrayList<>(); // 이 모집글에 참여한 멤버 목록

}
