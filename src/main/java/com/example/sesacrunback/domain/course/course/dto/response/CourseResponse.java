package com.example.sesacrunback.domain.course.course.dto.response;

import com.example.sesacrunback.domain.course.course.entity.Course;
import com.example.sesacrunback.domain.course.course.entity.enums.CourseStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class CourseResponse {
    private final Long id;
    private final Long instructorId;
    private final String instructor;  // 강사 이름
    private final String title;
    private final String description;
    private final String thumbnail;
    private final String category;
    private final String level;       // 난이도 (한글)
    private final String language;    // 언어 (한글)
    private final Integer price;
    private final Integer studentCount;
    private final CourseStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static CourseResponse from(Course course) {
        String instructorName = "강사";
        try {
            if (course.getInstructor() != null) {
                instructorName = course.getInstructor().getName();
            }
        } catch (Exception e) {
            // 프록시 초기화 실패 시 기본값 사용
            instructorName = "강사";
        }

        return new CourseResponse(
                course.getId(),
                course.getInstructorId(),
                instructorName,
                course.getTitle(),
                course.getDescription(),
                course.getThumbnail(),
                course.getCategory(),
                course.getLevel().getKorean(),
                course.getLanguage().getKorean(),
                course.getPrice(),
                course.getStudentCount(),
                course.getStatus(),
                course.getCreatedAt(),
                course.getUpdatedAt()
        );
    }
}
