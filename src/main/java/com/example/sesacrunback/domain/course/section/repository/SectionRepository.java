package com.example.sesacrunback.domain.course.section.repository;

import com.example.sesacrunback.domain.course.section.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
}
