package com.example.sesacrunback.domain.course.course.service;

import com.example.sesacrunback.domain.course.course.dto.request.CreateCourseRequest;
import com.example.sesacrunback.domain.course.course.dto.response.CourseDetailResponse;
import com.example.sesacrunback.domain.course.course.dto.response.CourseResponse;
import com.example.sesacrunback.domain.course.course.entity.Course;
import com.example.sesacrunback.domain.course.course.repository.CourseRepository;
import com.example.sesacrunback.domain.course.lecture.entity.Lecture;
import com.example.sesacrunback.domain.user.entity.User;
import com.example.sesacrunback.global.exception.CustomException;
import com.example.sesacrunback.global.exception.ErrorCode;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseService {

    private final CourseRepository courseRepository;
    private final EntityManager entityManager;

    /**
     * 현재 로그인한 사용자 ID 반환 (더미)
     */
    private Long getCurrentUserId() {
        return 1L; // 임시 강의자 ID
    }

    /**
     * 강의 생성 (Phase 1: 생성 즉시 게시)
     */
    @Transactional
    public CourseResponse createCourse(CreateCourseRequest request) {
        log.info("Creating new course: {}", request.getTitle());

        Long instructorId = getCurrentUserId();

        // User 프록시 생성 (실제 DB 조회 없이 참조만 생성)
        User instructor = entityManager.getReference(User.class, instructorId);

        // DTO가 엔티티 변환 및 조립 책임을 모두 담당
        Course course = request.toEntity(instructor);

        return CourseResponse.from(courseRepository.save(course));
    }

    public CourseDetailResponse getCourseDetail(Long courseId) {
        log.info("Getting course detail for ID: {}", courseId);

        // sections 조회 (lectures는 @BatchSize로 자동 로딩)
        Course course = courseRepository.findDetailWithSections(courseId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        return CourseDetailResponse.from(course);
    }

    /**
     * 전체 강의 목록 조회 (페이징)
     */
    public Page<CourseResponse> getAllCourses(Pageable pageable) {
        log.info("Getting all courses");

        return courseRepository.findAll(pageable)
                .map(CourseResponse::from);
    }

    /**
     * 카테고리별 강의 목록 조회
     */
    public Page<CourseResponse> getCoursesByCategory(String category, Pageable pageable) {
        log.info("Getting courses by category: {}", category);

        return courseRepository.findByCategory(category, pageable)
                .map(CourseResponse::from);
    }

    /**
     * 강의 검색
     */
    public Page<CourseResponse> searchCourses(String keyword, Pageable pageable) {
        log.info("Searching courses with keyword: {}", keyword);

        return courseRepository.searchCourses(keyword, pageable)
                .map(CourseResponse::from);
    }

    /**
     * 인기 강의 목록 조회
     */
    public Page<CourseResponse> getPopularCourses(Pageable pageable) {
        log.info("Getting popular courses");

        // 수강생 수 내림차순 정렬
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "studentCount")
        );

        return courseRepository.findAll(sortedPageable)
                .map(CourseResponse::from);
    }

    /**
     * 강사의 강의 목록 조회
     */
    public List<CourseResponse> getMyCourses() {
        Long instructorId = getCurrentUserId();
        log.info("Getting courses for instructor ID: {}", instructorId);

        return courseRepository.findByInstructor_Id(instructorId)
                .stream()
                .map(CourseResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 강의 삭제
     */
    @Transactional
    public void deleteCourse(Long courseId) {
        log.info("Deleting course with ID: {}", courseId);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        // 강사 본인 확인
        Long currentUserId = getCurrentUserId();
        if (!course.getInstructorId().equals(currentUserId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        courseRepository.delete(course);
    }
}
