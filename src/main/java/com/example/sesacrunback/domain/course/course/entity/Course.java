package com.example.sesacrunback.domain.course.course.entity;

import com.example.sesacrunback.domain.course.course.entity.enums.CourseLanguage;
import com.example.sesacrunback.domain.course.course.entity.enums.CourseLevel;
import com.example.sesacrunback.domain.course.course.entity.enums.CourseStatus;
import com.example.sesacrunback.domain.course.section.entity.Section;
import com.example.sesacrunback.domain.user.entity.User;
import com.example.sesacrunback.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id", nullable = false)
    private User instructor;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String detailedDescription;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String thumbnail;

    @Column(nullable = false)
    private String category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseLevel level;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseLanguage language;

    @Column(nullable = false)
    private Integer price;

    /**
     * 수강생 수 캐시 값
     * - ACTIVE Enrollment 수를 기준으로 계산된 결과
     * - Enrollment COUNT 쿼리 비용을 줄이기 위한 캐시 필드
     * - 최종 권위 데이터는 Enrollment (정확한 수치는 Enrollment 테이블 조회)
     * - 조회/정렬/통계 목적으로 사용
     * - Eventual Consistency: 트랜잭션 커밋 전까지 실제 Enrollment 수와 일시적 차이가 발생할 수 있음
     */
    @Column(nullable = false)
    private Integer studentCount = 0;

    /**
     * Optimistic Lock을 위한 버전 필드
     * - 동시성 제어: 동시에 여러 결제/환불이 발생해도 studentCount 일관성 유지
     * - OptimisticLockException 발생 시 트랜잭션 롤백
     * - JPA가 자동으로 관리 (초기값 0, 수정 시 자동 증가)
     */
    @Version
    private Long version;

    @ElementCollection
    @CollectionTable(
        name = "course_features",
        joinColumns = @JoinColumn(name = "course_id")
    )
    @Column(name = "feature")
    private List<String> features = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseStatus status;

    @OneToMany(
        mappedBy = "course",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @OrderBy("order ASC")
    @org.hibernate.annotations.BatchSize(size = 10)
    private final List<Section> sections = new ArrayList<>();

    /* ================= 생성 ================= */

    /**
     * 생성 즉시 게시되는 강의 (Phase 1)
     * Phase 2에서 승인 구조 도입 시 ofPending() 추가 예정
     */
    public static Course ofPublished(
            User instructor,
            String title,
            String description,
            String detailedDescription,
            String thumbnail,
            String category,
            CourseLevel level,
            CourseLanguage language,
            Integer price,
            List<String> features
    ) {
        Course course = new Course();
        course.instructor = instructor;
        course.title = title;
        course.description = description;
        course.detailedDescription = detailedDescription;
        course.thumbnail = thumbnail;
        course.category = category;
        course.level = level;
        course.language = language;
        course.price = price;
        course.features = features;
        course.status = CourseStatus.PUBLISHED;
        course.studentCount = 0;
        return course;
    }

    /* ================= 상태 ================= */

    public void archiveByInstructor() {
        this.status = CourseStatus.ARCHIVED;
    }

    /* ================= 구조 (Create 전용) ================= */

    public Section addSectionForCreate(String title, int order) {
        Section section = Section.builder()
                .course(this)
                .title(title)
                .order(order)
                .build();
        this.sections.add(section);
        return section;
    }

    /* ================= Update ================= */

    public void updateCourse(
            String title,
            String description,
            String detailedDescription,
            String thumbnail,
            String category,
            CourseLevel level,
            CourseLanguage language,
            Integer price,
            List<String> features
    ) {
        this.title = title;
        this.description = description;
        this.detailedDescription = detailedDescription;
        this.thumbnail = thumbnail;
        this.category = category;
        this.level = level;
        this.language = language;
        this.price = price;
        this.features = features != null ? features : new ArrayList<>();
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeDescription(String description) {
        this.description = description;
    }

    public void changeDetailedDescription(String detailedDescription) {
        this.detailedDescription = detailedDescription;
    }

    public void changeThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void changeCategory(String category) {
        this.category = category;
    }

    public void changePrice(Integer price) {
        this.price = price;
    }

    public void changeFeatures(List<String> features) {
        this.features = features;
    }

    /* ================= 비즈니스 로직 ================= */

    /**
     * 수강생 수 증가
     * - Enrollment가 ACTIVE로 새로 생성될 때만 호출
     */
    public void increaseStudentCount() {
        this.studentCount++;
    }

    /**
     * 수강생 수 감소
     * - Enrollment.status가 ACTIVE → CANCELED로 전이될 때만 호출
     * - 0 미만으로 내려가지 않도록 방어
     */
    public void decreaseStudentCount() {
        if (this.studentCount > 0) {
            this.studentCount--;
        }
    }

    /* ================= 편의 메서드 ================= */

    /**
     * instructor의 ID 반환 (프록시여도 ID는 가져올 수 있음)
     */
    public Long getInstructorId() {
        return instructor != null ? instructor.getId() : null;
    }

    /**
     * 강의 소유자인지 판단
     */
    public boolean isOwner(Long userId) {
        return this.instructor != null
            && this.instructor.getId().equals(userId);
    }
}
