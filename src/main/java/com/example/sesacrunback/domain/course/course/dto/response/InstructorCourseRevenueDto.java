package com.example.sesacrunback.domain.course.course.dto.response;

import com.example.sesacrunback.domain.course.course.entity.enums.CourseStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 강사 대시보드 - 운영 중인 강의 카드 요약 DTO
 * - id, title, thumbnail, price, studentCount, status, revenue 포함
 * - JPQL DTO Projection 전용
 */
@Getter
@RequiredArgsConstructor
public class InstructorCourseRevenueDto {

    private final Long id;

    private final String title;

    private final String thumbnail;

    private final Integer price;

    private final Integer studentCount;

    private final CourseStatus status;

    private final Long revenue;
}
