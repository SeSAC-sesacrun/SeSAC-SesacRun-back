package com.example.sesacrunback.domain.course.course.dto.response;

import com.example.sesacrunback.domain.course.course.entity.Course;
import com.example.sesacrunback.domain.course.course.entity.enums.CourseStatus;
import com.example.sesacrunback.domain.course.section.dto.response.SectionResponse;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CourseDetailResponse {
    private final Long id;
    private final Long instructorId;
    private final String title;
    private final String description;
    private final String detailedDescription;
    private final String thumbnail;
    private final String category;
    private final Integer price;
    private final Integer originalPrice;
    private final Integer discount;
    private final Double rating;
    private final Integer reviewCount;
    private final Integer studentCount;
    private final List<String> features;
    private final CourseStatus status;
    private final List<SectionResponse> sections;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private CourseDetailResponse(Long id, Long instructorId, String title, String description,
                                 String detailedDescription, String thumbnail, String category, Integer price,
                                 Integer originalPrice, Integer discount, Double rating, Integer reviewCount,
                                 Integer studentCount, List<String> features, CourseStatus status,
                                 List<SectionResponse> sections, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.instructorId = instructorId;
        this.title = title;
        this.description = description;
        this.detailedDescription = detailedDescription;
        this.thumbnail = thumbnail;
        this.category = category;
        this.price = price;
        this.originalPrice = originalPrice;
        this.discount = discount;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.studentCount = studentCount;
        this.features = features;
        this.status = status;
        this.sections = sections;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static CourseDetailResponse from(Course course) {
        return new CourseDetailResponse(
                course.getId(),
                course.getInstructor().getId(),
                course.getTitle(),
                course.getDescription(),
                course.getDetailedDescription(),
                course.getThumbnail(),
                course.getCategory(),
                course.getPrice(),
                course.getOriginalPrice(),
                course.getDiscount(),
                course.getRating(),
                course.getReviewCount(),
                course.getStudentCount(),
                course.getFeatures(),
                course.getStatus(),
                course.getSections().stream()
                        .map(SectionResponse::from)
                        .collect(Collectors.toList()),
                course.getCreatedAt(),
                course.getUpdatedAt()
        );
    }
}
