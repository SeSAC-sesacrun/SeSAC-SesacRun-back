package com.example.sesacrunback.domain.course.course.entity;

import com.example.sesacrunback.domain.course.course.converter.StringListJsonConverter;
import com.example.sesacrunback.domain.course.course.entity.enums.CourseStatus;
import com.example.sesacrunback.domain.course.section.entity.Section;
import com.example.sesacrunback.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Course extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id", nullable = false, insertable = false, updatable = false)
    private com.example.sesacrunback.domain.user.entity.User instructor;

    @Column(name = "instructor_id", nullable = false)
    private Long instructorId;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(length = 500)
    private String description;

    @Column(columnDefinition = "TEXT")
    private String detailedDescription;

    @Column(columnDefinition = "TEXT")
    private String thumbnail;

    @Column(length = 100)
    private String category;

    @Column(nullable = false)
    @Builder.Default
    private Integer price = 0;

    @Column
    private Integer originalPrice;

    @Column
    @Builder.Default
    private Integer discount = 0;

    @Column(nullable = false)
    @Builder.Default
    private Double rating = 0.0;

    @Column(nullable = false)
    @Builder.Default
    private Integer reviewCount = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer studentCount = 0;

    @Convert(converter = StringListJsonConverter.class)
    @Column(columnDefinition = "JSON")
    private List<String> features;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private CourseStatus status = CourseStatus.PUBLISHED;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("order ASC")
    @org.hibernate.annotations.BatchSize(size = 10)
    @Builder.Default
    private List<Section> sections = new ArrayList<>();

    /**
     * 비즈니스 로직
     */
    public void updateCourseInfo(String title, String description, String detailedDescription,
                                  String thumbnail, String category, Integer price, Integer originalPrice,
                                  Integer discount, List<String> features) {
        if (title != null && !title.isBlank()) {
            this.title = title;
        }
        if (description != null) {
            this.description = description;
        }
        if (detailedDescription != null) {
            this.detailedDescription = detailedDescription;
        }
        if (thumbnail != null) {
            this.thumbnail = thumbnail;
        }
        if (category != null) {
            this.category = category;
        }
        if (price != null) {
            this.price = price;
        }
        if (originalPrice != null) {
            this.originalPrice = originalPrice;
        }
        if (discount != null) {
            this.discount = discount;
        }
        if (features != null) {
            this.features = features;
        }
    }

    public void publish() {
        this.status = CourseStatus.PUBLISHED;
    }

    public void archive() {
        this.status = CourseStatus.ARCHIVED;
    }

    public void addSection(Section section) {
        this.sections.add(section);
    }

    public void incrementStudentCount() {
        this.studentCount++;
    }

    /**
     * 새로운 리뷰 평점 추가 및 평균 재계산
     * @param newRating 새로 추가된 리뷰의 평점
     */
    public void addReview(Double newRating) {
        this.rating = (this.rating * this.reviewCount + newRating) / (this.reviewCount + 1);
        this.reviewCount++;
    }
}
