package com.example.sesacrunback.domain.course.lecture.entity;

import com.example.sesacrunback.domain.course.section.entity.Section;
import com.example.sesacrunback.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lectures")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Lecture extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false)
    @Builder.Default
    private Integer duration = 0; // 영상 길이 (초)

    @Column(nullable = false, name = "lecture_order")
    private Integer order;

    @Column(nullable = false, length = 500)
    private String videoUrl; // YouTube URL

    @Column(nullable = false)
    @Builder.Default
    private Boolean isFree = false;

    /**
     * 비즈니스 로직
     */
    public void updateLectureInfo(String title, Integer duration, String videoUrl, Boolean isFree) {
        if (title != null && !title.isBlank()) {
            this.title = title;
        }
        if (duration != null) {
            this.duration = duration;
        }
        if (videoUrl != null && !videoUrl.isBlank()) {
            this.videoUrl = videoUrl;
        }
        if (isFree != null) {
            this.isFree = isFree;
        }
    }

    public void updateOrder(Integer order) {
        if (order != null) {
            this.order = order;
        }
    }

    public void setSection(Section section) {
        this.section = section;
    }
}
