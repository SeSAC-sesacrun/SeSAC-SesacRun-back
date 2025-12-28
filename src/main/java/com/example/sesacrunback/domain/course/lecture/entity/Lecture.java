package com.example.sesacrunback.domain.course.lecture.entity;

import com.example.sesacrunback.domain.course.section.entity.Section;
import com.example.sesacrunback.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "lectures")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
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
    private Integer duration; // 영상 길이 (초)

    @Column(nullable = false, name = "lecture_order")
    private Integer order;

    @Column(nullable = false, length = 500)
    private String videoUrl; // YouTube / S3 URL

    @Column(nullable = false)
    private Boolean isFree;

    /* ========= 생성 책임 ========= */

    public static Lecture of(
            Section section,
            String title,
            Integer order,
            String videoUrl,
            Integer duration,
            Boolean isFree
    ) {
        return Lecture.builder()
                .section(section)
                .title(title)
                .order(order)
                .videoUrl(videoUrl)
                .duration(duration != null ? duration : 0)
                .isFree(isFree != null ? isFree : false)
                .build();
    }

    /* ========= 비즈니스 로직 ========= */

    public void updateLectureInfo(
            String title,
            Integer duration,
            String videoUrl,
            Boolean isFree
    ) {
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
}
