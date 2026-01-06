package com.example.sesacrunback.domain.enrollment.repository;

import com.example.sesacrunback.domain.course.course.entity.Course;
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

    @Query("""
        SELECT e.course
        FROM Enrollment e
        WHERE e.user.id = :userId
          AND e.status = 'ACTIVE'
    """)
    Page<Course> findEnrolledCourses(
        @Param("userId") Long userId,
        Pageable pageable
    );

    default boolean hasActiveEnrollment(Long userId, Long courseId) {
        return existsByUserIdAndCourseIdAndStatus(
            userId,
            courseId,
            EnrollmentStatus.ACTIVE
        );
    }
}
