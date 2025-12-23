package com.example.sesacrunback.domain.course.course.dto.response;

import com.example.sesacrunback.domain.course.course.entity.Course;
import com.example.sesacrunback.domain.course.course.entity.enums.CourseStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CourseResponse {
    private final Long id;
    private final Long instructorId;
    private final String title;
    private final String description;
    private final String thumbnail;
    private final String category;
    private final Integer price;
    private final Integer originalPrice;
    private final Integer discount;
    private final Double rating;
    private final Integer reviewCount;
    private final Integer studentCount;
    private final CourseStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private CourseResponse(Long id, Long instructorId, String title,
                           String description, String thumbnail, String category, Integer price,
                           Integer originalPrice, Integer discount, Double rating, Integer reviewCount,
                           Integer studentCount, CourseStatus status, LocalDateTime createdAt,
                           LocalDateTime updatedAt) {
        this.id = id;
        this.instructorId = instructorId;
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
        this.category = category;
        this.price = price;
        this.originalPrice = originalPrice;
        this.discount = discount;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.studentCount = studentCount;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static CourseResponse from(Course course) {
        return new CourseResponse(
                course.getId(),
                course.getInstructorId(),
                course.getTitle(),
                course.getDescription(),
                course.getThumbnail(),
                course.getCategory(),
                course.getPrice(),
                course.getOriginalPrice(),
                course.getDiscount(),
                course.getRating(),
                course.getReviewCount(),
                course.getStudentCount(),
                course.getStatus(),
                course.getCreatedAt(),
                course.getUpdatedAt()
        );
    }
}
