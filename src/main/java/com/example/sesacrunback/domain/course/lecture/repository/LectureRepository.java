package com.example.sesacrunback.domain.course.lecture.repository;

import com.example.sesacrunback.domain.course.lecture.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {

    /**
     * 같은 Section 내에서 특정 order를 가진 Lecture가 존재하는지 확인
     * (수정 시 자기 자신은 제외하고 확인)
     */
    boolean existsBySectionIdAndOrderAndIdNot(Long sectionId, Integer order, Long id);

    /**
     * 같은 Section 내에서 특정 order를 가진 Lecture가 존재하는지 확인
     * (생성 시 사용)
     */
    boolean existsBySectionIdAndOrder(Long sectionId, Integer order);
}
