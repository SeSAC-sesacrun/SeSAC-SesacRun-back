package com.example.sesacrunback.domain.course.course.repository;

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
     * - sections 함께 조회
     * - lectures는 SectionRepository에서 별도 조회
     * - MultipleBagFetchException 방지
     */
    @Query("""
        SELECT DISTINCT c
        FROM Course c
        LEFT JOIN FETCH c.sections s
        WHERE c.id = :id
    """)
    Optional<Course> findDetailWithSections(@Param("id") Long id);

    /* ===============================
     * 목록 조회
     * =============================== */

    /**
     * 전체 강의 목록 (Paging)
     * - JpaRepository의 findAll(Pageable)을 그대로 사용
     */

    /**
     * 카테고리별 강의 목록
     */
    Page<Course> findByCategory(String category, Pageable pageable);

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
}
