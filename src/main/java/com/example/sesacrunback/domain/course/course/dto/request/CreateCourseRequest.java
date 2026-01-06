package com.example.sesacrunback.domain.course.course.dto.request;

import com.example.sesacrunback.domain.course.course.entity.Course;
import com.example.sesacrunback.domain.course.course.entity.enums.CourseLanguage;
import com.example.sesacrunback.domain.course.course.entity.enums.CourseLevel;
import com.example.sesacrunback.domain.course.section.entity.Section;
import com.example.sesacrunback.domain.course.lecture.entity.Lecture;
import com.example.sesacrunback.domain.user.entity.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCourseRequest {

    @NotBlank(message = "강의 제목은 필수입니다")
    private String title;

    @NotBlank(message = "간단 소개는 필수입니다")
    private String description;

    @NotBlank(message = "상세 설명은 필수입니다")
    private String detailedDescription;

    @NotBlank(message = "썸네일 URL은 필수입니다")
    private String thumbnail;

    @NotBlank(message = "카테고리는 필수입니다")
    private String category;

    @NotNull(message = "난이도는 필수입니다")
    private CourseLevel level;

    @NotNull(message = "언어는 필수입니다")
    private CourseLanguage language;

    @NotNull(message = "가격은 필수입니다")
    @Min(value = 0, message = "가격은 0원 이상이어야 합니다")
    private Integer price;

    private List<String> features = new ArrayList<>();

    @Valid
    private List<CreateSectionRequest> sections = new ArrayList<>();

    /* ================= Course ================= */

    public Course toEntity(User instructor) {
        Course course = Course.ofPublished(
            instructor,
            title,
            description,
            detailedDescription,
            thumbnail,
            category,
            level,
            language,
            price,
            features
        );

        sections.forEach(sectionReq -> sectionReq.toEntity(course));
        return course;
    }

    /* ================= Section ================= */

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateSectionRequest {

        @NotBlank(message = "섹션 제목은 필수입니다")
        private String title;

        @NotNull(message = "섹션 순서는 필수입니다")
        private Integer order;

        @Valid
        private List<CreateLectureRequest> lectures = new ArrayList<>();

        private int orderOrZero() {
            return order != null ? order : 0;
        }

        private List<CreateLectureRequest> lecturesOrEmpty() {
            return lectures != null ? lectures : List.of();
        }

        public Section toEntity(Course course) {
            Section section = course.addSectionForCreate(title, orderOrZero());
            lecturesOrEmpty().forEach(lectureReq -> lectureReq.toEntity(section));
            return section;
        }
    }

    /* ================= Lecture ================= */

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateLectureRequest {

        @NotBlank(message = "강의 제목은 필수입니다")
        private String title;

        @NotBlank(message = "영상 URL은 필수입니다")
        private String videoUrl;

        @NotNull(message = "영상 길이는 필수입니다")
        @Min(value = 1, message = "영상 길이는 1초 이상이어야 합니다")
        private Integer duration;

        @NotNull(message = "강의 순서는 필수입니다")
        private Integer order;

        private Boolean isFree;

        private boolean freeOrFalse() {
            return Boolean.TRUE.equals(isFree);
        }

        public Lecture toEntity(Section section) {
            Lecture lecture = Lecture.of(
                section,
                title,
                order,
                videoUrl,
                duration,
                freeOrFalse()
            );

            section.addLecture(lecture);
            return lecture;
        }
    }
}
