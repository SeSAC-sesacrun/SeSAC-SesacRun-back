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
    private final String title;
    private final String description;
    private final String thumbnail;
    private final String category;
    private final Integer price;
    private final Integer studentCount;
    private final CourseStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static CourseResponse from(Course course) {
        return new CourseResponse(
                course.getId(),
                course.getInstructorId(),
                course.getTitle(),
                course.getDescription(),
                course.getThumbnail(),
                course.getCategory(),
                course.getPrice(),
                course.getStudentCount(),
                course.getStatus(),
                course.getCreatedAt(),
                course.getUpdatedAt()
        );
    }
}
