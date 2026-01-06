package com.example.sesacrunback.domain.course.course.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EnrolledCourseResponse {

    private Long enrollmentId;
    private LocalDateTime enrolledAt;

    private Long courseId;
    private String title;
    private String thumbnail;
    private int price;

    private Long instructorId;
    private String instructorName;
}
