package com.example.sesacrunback.domain.enrollment.repository;

import com.example.sesacrunback.domain.course.course.dto.response.EnrolledCourseResponse;
import com.example.sesacrunback.domain.enrollment.entity.Enrollment;
import com.example.sesacrunback.domain.enrollment.entity.EnrollmentStatus;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    boolean existsByUserIdAndCourseIdAndStatus(
        Long userId,
        Long courseId,
        EnrollmentStatus status
    );

    Optional<Enrollment> findByUserIdAndCourseIdAndStatus(
        Long userId,
        Long courseId,
        EnrollmentStatus status
    );

    default boolean hasActiveEnrollment(Long userId, Long courseId) {
        return existsByUserIdAndCourseIdAndStatus(
            userId,
            courseId,
            EnrollmentStatus.ACTIVE
        );
    }

    /**
     * 수강 중인 강의 목록 조회 (DTO Projection)
     */
    @Query(
        value = """
            SELECT new com.example.sesacrunback.domain.course.course.dto.response.EnrolledCourseResponse(
                e.id,
                e.createdAt,
                c.id,
                c.title,
                c.thumbnail,
                c.price,
                i.id,
                i.name
            )
            FROM Enrollment e
            JOIN e.course c
            JOIN c.instructor i
            WHERE e.user.id = :userId
              AND e.status = 'ACTIVE'
        """,
        countQuery = """
            SELECT COUNT(e)
            FROM Enrollment e
            WHERE e.user.id = :userId
              AND e.status = 'ACTIVE'
        """
    )
    Page<EnrolledCourseResponse> findEnrolledCourses(
        @Param("userId") Long userId,
        Pageable pageable
    );
}
