package com.example.sesacrunback.domain.course.section.service;

import com.example.sesacrunback.domain.course.course.entity.Course;
import com.example.sesacrunback.domain.course.course.repository.CourseRepository;
import com.example.sesacrunback.domain.course.section.dto.request.SectionCreateReqDto;
import com.example.sesacrunback.domain.course.section.dto.request.SectionUpdateReqDto;
import com.example.sesacrunback.domain.course.section.dto.response.SectionResponse;
import com.example.sesacrunback.domain.course.section.entity.Section;
import com.example.sesacrunback.domain.course.section.repository.SectionRepository;
import com.example.sesacrunback.global.exception.CustomException;
import com.example.sesacrunback.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SectionService {

    private final CourseRepository courseRepository;
    private final SectionRepository sectionRepository;

    /* ================= 생성 ================= */

    @Transactional
    public Long createSection(Long courseId, SectionCreateReqDto reqDto, Long userId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CustomException(ErrorCode.COURSE_NOT_FOUND));

        validateCourseOwner(course, userId);

        int order = sectionRepository.findTopByCourseIdOrderByOrderDesc(courseId)
                .map(section -> section.getOrder() + 1)
                .orElse(0);

        Section section = Section.of(course, reqDto.getTitle(), order);
        sectionRepository.save(section);

        return section.getId();
    }

    /* ================= 단건 조회 ================= */

    public SectionResponse viewSection(Long sectionId, Long userId) {

        Section section = getSectionById(sectionId);
        validateSectionOwner(section, userId);

        return SectionResponse.from(section);
    }

    /* ================= 수정 ================= */

    @Transactional
    public Long updateSection(Long sectionId, SectionUpdateReqDto reqDto, Long userId) {

        Section section = getSectionById(sectionId);
        validateSectionOwner(section, userId);

        // Order 중복 검증 (자기 자신 제외)
        if (sectionRepository.existsByCourseIdAndOrderAndIdNot(
                section.getCourse().getId(), reqDto.getOrder(), sectionId)) {
            throw new CustomException(ErrorCode.SECTION_ORDER_DUPLICATE);
        }

        section.updateSection(
                reqDto.getTitle(),
                reqDto.getOrder()
        );

        return section.getId();
    }

    /* ================= 삭제 ================= */

    @Transactional
    public void deleteSection(Long sectionId, Long userId) {

        Section section = getSectionById(sectionId);
        validateSectionOwner(section, userId);

        sectionRepository.delete(section);
    }

    /* ================= 내부 공통 ================= */

    private Section getSectionById(Long sectionId) {
        return sectionRepository.findById(sectionId)
                .orElseThrow(() -> new CustomException(ErrorCode.SECTION_NOT_FOUND));
    }

    private void validateCourseOwner(Course course, Long userId) {
        if (!course.isOwner(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }

    private void validateSectionOwner(Section section, Long userId) {
        if (!section.isOwnedBy(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }
}
