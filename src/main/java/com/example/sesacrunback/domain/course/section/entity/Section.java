package com.example.sesacrunback.domain.course.section.entity;

import com.example.sesacrunback.domain.course.course.entity.Course;
import com.example.sesacrunback.domain.course.lecture.entity.Lecture;
import com.example.sesacrunback.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sections")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Section extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, name = "section_order")
    private Integer order;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("order ASC")
    @org.hibernate.annotations.BatchSize(size = 20)
    @Builder.Default
    private List<Lecture> lectures = new ArrayList<>();

    /* ========= 생성 ========= */

    public static Section of(Course course, String title, Integer order) {
        return Section.builder()
                .course(course)
                .title(title)
                .order(order)
                .build();
    }

    /**
     * 비즈니스 로직
     */
    /* ========= 구조 (Create 전용) ========= */

    public void addLecture(Lecture lecture) {
        this.lectures.add(lecture);
    }

    /* ========= 업데이트 ========= */

    public void updateSection(String title, Integer order) {
        this.title = title;
        this.order = order;
    }

    /* ========= 권한 위임 ========= */

    public boolean isOwnedBy(Long userId) {
        return this.course.isOwner(userId);
    }
}
