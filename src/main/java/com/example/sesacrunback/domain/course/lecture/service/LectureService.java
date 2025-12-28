package com.example.sesacrunback.domain.course.lecture.service;


import com.example.sesacrunback.domain.course.lecture.dto.request.LectureCreateReqDto;
import com.example.sesacrunback.domain.course.lecture.dto.request.LectureUpdateReqDto;
import com.example.sesacrunback.domain.course.lecture.dto.response.LectureResponse;
import com.example.sesacrunback.domain.course.lecture.entity.Lecture;
import com.example.sesacrunback.domain.course.lecture.repository.LectureRepository;
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
public class LectureService {

    private final LectureRepository lectureRepository;
    private final SectionRepository sectionRepository;

    /* ================= 생성 ================= */

    @Transactional
    public Long createLecture(LectureCreateReqDto reqDto, Long sectionId, Long userId) {

        Section section = getSectionById(sectionId);
        validateSectionOwner(section, userId);

        Lecture lecture = lectureRepository.save(
                reqDto.toEntity(section)
        );

        return lecture.getId();
    }

    /* ================= 단건 조회 ================= */

    public LectureResponse viewLecture(Long lectureId, Long userId) {

        Lecture lecture = getLectureById(lectureId);
        validateLectureOwner(lecture, userId);

        return LectureResponse.from(lecture);
    }

    /* ================= 수정 ================= */

    @Transactional
    public Long updateLecture(Long lectureId, LectureUpdateReqDto reqDto, Long userId) {

        Lecture lecture = getLectureById(lectureId);
        validateLectureOwner(lecture, userId);

        lecture.updateLectureInfo(
                reqDto.getTitle(),
                reqDto.getDuration(),
                reqDto.getVideoUrl(),
                reqDto.getIsFree()
        );

        lecture.updateOrder(reqDto.getOrder());

        return lecture.getId();
    }

    /* ================= 삭제 ================= */

    @Transactional
    public void deleteLecture(Long lectureId, Long userId) {

        Lecture lecture = getLectureById(lectureId);
        validateLectureOwner(lecture, userId);

        lectureRepository.delete(lecture);
    }

    /* ================= 내부 공통 ================= */

    private Lecture getLectureById(Long lectureId) {
        return lectureRepository.findById(lectureId)
                .orElseThrow(() -> new CustomException(ErrorCode.LECTURE_NOT_FOUND));
    }

    private Section getSectionById(Long sectionId) {
        return sectionRepository.findById(sectionId)
                .orElseThrow(() -> new CustomException(ErrorCode.SECTION_NOT_FOUND));
    }

    private void validateSectionOwner(Section section, Long userId) {
        if (!section.isOwnedBy(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }

    private void validateLectureOwner(Lecture lecture, Long userId) {
        if (!lecture.getSection().isOwnedBy(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }
}
