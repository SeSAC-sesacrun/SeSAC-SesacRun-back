package com.example.sesacrunback.domain.course.lecture.entity;

import com.example.sesacrunback.domain.course.section.entity.Section;
import com.example.sesacrunback.global.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "lectures")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lecture extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

    @Column(nullable = false)
    private String title; // 강의 제목

    @Column(nullable = false)
    private String youtubeUrl; // 유튜브 URL

    @Column(nullable = false)
    private String duration; // 강의 시간

    @Column(nullable = false)
    private Boolean isFree; // 무료 여부

    @Column(nullable = false)
    private Integer orderIndex; // 순서

    @ManyToOne
    @JoinColumn(name = "section_id", nullable = false)
    private Section section; // 소속된 섹션

}
