package com.example.sesacrunback.domain.course.section.entity;

import com.example.sesacrunback.domain.course.course.entity.Course;
import com.example.sesacrunback.domain.course.lecture.entity.Lecture;
import com.example.sesacrunback.global.common.entity.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "sections")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Section extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

    @Column(nullable = false)
    private String title; // 섹션 제목

    @Column(nullable = false)
    private Integer orderIndex; // 순서

    @ManyToOne
    @JoinColumn(name = "course_id" , nullable = false)
    private Course course; // 소속된 강의

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lecture> lectures = new ArrayList<>(); // 섹션에 포함된 강의 목록
}
