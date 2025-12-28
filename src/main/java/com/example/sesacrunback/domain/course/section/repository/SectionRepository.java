package com.example.sesacrunback.domain.course.section.repository;

import com.example.sesacrunback.domain.course.section.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface SectionRepository extends JpaRepository<Section, Long> {

    /**
     * 해당 코스에서 가장 마지막 순서의 섹션 조회
     * (섹션 생성 시 order 계산용)
     */
    Optional<Section> findTopByCourseIdOrderByOrderDesc(Long courseId);

    /**
     * 코스에 속한 섹션 목록 조회 (순서 오름차순)
     * - 필요 시 리스트 조회 API용
     */
    List<Section> findByCourseIdOrderByOrderAsc(Long courseId);
}