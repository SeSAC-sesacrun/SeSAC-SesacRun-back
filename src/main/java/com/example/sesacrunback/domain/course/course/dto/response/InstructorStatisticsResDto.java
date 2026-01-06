package com.example.sesacrunback.domain.course.course.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 강사 통계 정보 응답 DTO
 * - 강사 대시보드에서 사용
 * - 총 강의 수, 총 수강생 수, 총 수익 정보 제공
 */
@Getter
@RequiredArgsConstructor
public class InstructorStatisticsResDto {

    private final Integer totalCourses;

    private final Long totalStudents;

    private final Long totalRevenue;

    public static InstructorStatisticsResDto of(
            Integer totalCourses,
            Long totalStudents,
            Long totalRevenue
    ) {
        return new InstructorStatisticsResDto(
                totalCourses,
                totalStudents,
                totalRevenue
        );
    }
}
