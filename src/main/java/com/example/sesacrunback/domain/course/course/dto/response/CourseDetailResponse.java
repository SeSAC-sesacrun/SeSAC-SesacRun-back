package com.example.sesacrunback.domain.course.course.dto.response;

import com.example.sesacrunback.domain.course.course.entity.Course;
import com.example.sesacrunback.domain.course.course.entity.enums.CourseStatus;
import com.example.sesacrunback.domain.course.section.dto.response.SectionResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class CourseDetailResponse {
    private final Long id;
    private final Long instructorId;
    private final String title;
    private final String description;
    private final String detailedDescription;
    private final String thumbnail;
    private final String category;
    private final Integer price;
    private final Integer studentCount;
    private final List<String> features;
    private final CourseStatus status;

    /** 버튼 분기용 */
    private final boolean canWatch;

    private final List<SectionResponse> sections;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static CourseDetailResponse from(Course course) {
        return new CourseDetailResponse(
                course.getId(),
                course.getInstructorId(),
                course.getTitle(),
                course.getDescription(),
                course.getDetailedDescription(),
                course.getThumbnail(),
                course.getCategory(),
                course.getPrice(),
                course.getStudentCount(),
                course.getFeatures(),
                course.getStatus(),
                false,
                course.getSections().stream()
                        .map(SectionResponse::from)
                        .collect(Collectors.toList()),
                course.getCreatedAt(),
                course.getUpdatedAt()
        );
    }

    public static CourseDetailResponse from(
            Course course,
            CourseViewContext ctx,
            boolean canWatch
    ) {
        return new CourseDetailResponse(
                course.getId(),
                course.getInstructorId(),
                course.getTitle(),
                course.getDescription(),
                course.getDetailedDescription(),
                course.getThumbnail(),
                course.getCategory(),
                course.getPrice(),
                course.getStudentCount(),
                course.getFeatures(),
                course.getStatus(),
                canWatch,
                course.getSections().stream()
                        .map(s -> SectionResponse.from(s, ctx))
                        .collect(Collectors.toList()),
                course.getCreatedAt(),
                course.getUpdatedAt()
        );
    }
}
