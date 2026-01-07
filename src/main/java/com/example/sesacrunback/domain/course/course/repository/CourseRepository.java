package com.example.sesacrunback.domain.course.course.repository;

import com.example.sesacrunback.domain.course.course.dto.response.InstructorCourseRevenueDto;
import com.example.sesacrunback.domain.course.course.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    /* ===============================
     * 상세 조회 (Fetch 전략)
     * =============================== */

    /**
     * 강의 상세 조회
     * - instructor, sections 함께 조회
     * - lectures는 SectionRepository에서 별도 조회
     * - MultipleBagFetchException 방지
     */
    @Query("""
        SELECT DISTINCT c
        FROM Course c
        LEFT JOIN FETCH c.instructor
        LEFT JOIN FETCH c.sections s
        WHERE c.id = :id
    """)
    Optional<Course> findDetailWithSections(@Param("id") Long id);

    /* ===============================
     * 목록 조회
     * =============================== */

    /**
     * Section과 Lecture가 1개 이상 있는 강의만 조회
     * - JOIN을 통해 실제 콘텐츠가 있는 강의만 필터링
     * - DISTINCT로 중복 제거 (Section × Lecture 조합으로 row 증가 방지)
     * - fetch join 사용 X (Pageable과 함께 사용 시 페이징 깨짐)
     */
    @Query("""
        SELECT DISTINCT c
        FROM Course c
        JOIN c.sections s
        JOIN s.lectures l
        WHERE c.status = 'PUBLISHED'
    """)
    Page<Course> findAllWithContent(Pageable pageable);

    /**
     * 카테고리별 강의 목록
     * - Section과 Lecture가 1개 이상 있는 강의만 조회
     * - PUBLISHED 상태만 조회
     */
    @Query("""
        SELECT DISTINCT c
        FROM Course c
        JOIN c.sections s
        JOIN s.lectures l
        WHERE c.status = 'PUBLISHED'
          AND c.category = :category
    """)
    Page<Course> findByCategory(@Param("category") String category, Pageable pageable);

    /**
     * 강사별 강의 목록 (Paging)
     */
    Page<Course> findByInstructor_Id(Long instructorId, Pageable pageable);

    /* ===============================
     * 검색
     * =============================== */

    /**
     * 키워드 검색 (제목 OR 설명)
     */
    @Query("""
        SELECT c
        FROM Course c
        WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    Page<Course> searchCourses(
            @Param("keyword") String keyword,
            Pageable pageable
    );

    /* ===============================
     * 통계 (강사 대시보드용)
     * =============================== */

    /**
     * 강사별 강의 수
     */
    Long countByInstructor_Id(Long instructorId);

    /**
     * 강사별 총 수강생 수 (studentCount 합산)
     * - Course.studentCount는 ACTIVE Enrollment 수를 캐싱한 값
     * - 정확한 실시간 수치는 Enrollment COUNT 쿼리로 확인 가능
     * - JPQL SUM은 Long 반환 (Integer 필드도 Long 반환)
     */
    @Query("SELECT COALESCE(SUM(c.studentCount), 0) FROM Course c WHERE c.instructor.id = :instructorId")
    Long sumStudentCountByInstructor_Id(@Param("instructorId") Long instructorId);

    /**
     * 강사 대시보드 - 운영 중인 강의 목록 (수익 포함)
     * - DTO Projection으로 한 번의 쿼리로 Course + Revenue 조회
     * - OrderItem.price 기준 수익 계산 (강의별 실제 판매 가격)
     * - Order.status = 'COMPLETED'만 집계
     * - 스칼라 서브쿼리로 강의별 수익 정확히 계산 (중복 집계 방지)
     * - 정렬은 Pageable로 동적 처리
     */
    @Query("""
        SELECT new com.example.sesacrunback.domain.course.course.dto.response.InstructorCourseRevenueDto(
            c.id,
            c.title,
            c.thumbnail,
            c.price,
            c.studentCount,
            c.status,
            (SELECT COALESCE(SUM(oi.price), 0)
             FROM OrderItem oi
             JOIN oi.order o
             WHERE oi.course = c AND o.status = 'COMPLETED')
        )
        FROM Course c
        WHERE c.instructor.id = :instructorId
    """)
    Page<InstructorCourseRevenueDto> findInstructorCoursesWithRevenue(
            @Param("instructorId") Long instructorId,
            Pageable pageable
    );
}
