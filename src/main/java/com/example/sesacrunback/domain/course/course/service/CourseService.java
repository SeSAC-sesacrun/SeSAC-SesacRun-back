package com.example.sesacrunback.domain.course.course.service;

import com.example.sesacrunback.domain.course.course.dto.request.CourseUpdateReqDto;
import com.example.sesacrunback.domain.course.course.dto.request.CreateCourseRequest;
import com.example.sesacrunback.domain.course.course.dto.response.CourseDetailResponse;
import com.example.sesacrunback.domain.course.course.dto.response.CourseResponse;
import com.example.sesacrunback.domain.course.course.dto.response.CourseViewContext;
import com.example.sesacrunback.domain.course.course.dto.response.CourseWatchResponse;
import com.example.sesacrunback.domain.course.course.dto.response.EnrolledCourseResponse;
import com.example.sesacrunback.domain.course.course.dto.response.InstructorCourseRevenueDto;
import com.example.sesacrunback.domain.course.course.dto.response.InstructorStatisticsResDto;
import com.example.sesacrunback.domain.course.course.entity.Course;
import com.example.sesacrunback.domain.course.course.repository.CourseRepository;
import com.example.sesacrunback.domain.enrollment.entity.EnrollmentStatus;
import com.example.sesacrunback.domain.enrollment.repository.EnrollmentRepository;
import com.example.sesacrunback.domain.course.lecture.entity.Lecture;
import com.example.sesacrunback.domain.payment.repository.PaymentRepository;
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
    private final EnrollmentRepository enrollmentRepository;
    private final PaymentRepository paymentRepository;

    /**
     * 강의 생성 (Phase 1: 생성 즉시 게시)
     */
    @Transactional
    public CourseResponse createCourse(CreateCourseRequest request, Long instructorId) {
        log.info("Creating new course: {}", request.getTitle());

        // User 프록시 생성 (실제 DB 조회 없이 참조만 생성)
        User instructor = entityManager.getReference(User.class, instructorId);

        // DTO가 엔티티 변환 및 조립 책임을 모두 담당
        Course course = request.toEntity(instructor);

        return CourseResponse.from(courseRepository.save(course));
    }

    /**
     * 상세 페이지 (미리보기 + 버튼 분기)
     */
    public CourseDetailResponse getCourseDetail(Long courseId, Long userId) {
        log.info("Getting course detail for ID: {}", courseId);

        // sections 조회 (lectures는 @BatchSize로 자동 로딩)
        Course course = courseRepository.findDetailWithSections(courseId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        final boolean isUserLoggedIn = userId != null;
        final boolean isInstructor = isUserLoggedIn && course.isOwner(userId);
        final boolean hasPurchased = isUserLoggedIn && enrollmentRepository.hasActiveEnrollment(userId, courseId);
        final boolean canWatch = isInstructor || hasPurchased;

        CourseViewContext ctx = new CourseViewContext(isInstructor);

        return CourseDetailResponse.from(course, ctx, canWatch);
    }

    /**
     * Watch 페이지 (강사 or 구매자만 전체 시청)
     */
    public CourseWatchResponse getCourseForWatch(Long courseId, Long userId) {
        log.info("Getting course for watch - courseId: {}, userId: {}", courseId, userId);

        Course course = courseRepository.findDetailWithSections(courseId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        if (!course.isOwner(userId)
                && !enrollmentRepository.hasActiveEnrollment(userId, courseId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        return CourseWatchResponse.from(course);
    }

    /**
     * 전체 강의 목록 조회 (페이징)
     * - Section과 Lecture가 1개 이상 있는 강의만 반환
     */
    public Page<CourseResponse> getAllCourses(Pageable pageable) {
        log.info("Getting all courses with content");

        return courseRepository.findAllWithContent(pageable)
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
     * 강사의 강의 목록 조회 (페이징)
     * - 강의별 수익 포함 (Payment.amount 기준)
     */
    public Page<InstructorCourseRevenueDto> getMyCourses(Pageable pageable, Long instructorId) {
        log.info("Getting courses for instructor ID: {}", instructorId);

        return courseRepository.findInstructorCoursesWithRevenue(instructorId, pageable);
    }

    /**
     * 강사 통계 조회 (대시보드용)
     * - 총 강의 수
     * - 총 수강생 수 (Course.studentCount 합산)
     * - 총 수익 (COMPLETED 주문 기준)
     */
    public InstructorStatisticsResDto getMyStatistics(Long instructorId) {
        log.info("Getting statistics for instructor ID: {}", instructorId);

        // 1. 총 강의 수
        int totalCourses = courseRepository.countByInstructor_Id(instructorId).intValue();

        // 2. 총 수강생 수 (studentCount 합산) - JPQL SUM은 Long 반환
        Long totalStudents = courseRepository.sumStudentCountByInstructor_Id(instructorId);

        // 3. 총 수익 (Phase 1에서 만든 쿼리 사용)
        Long totalRevenue = paymentRepository.calculateTotalRevenueByInstructor(instructorId);

        return InstructorStatisticsResDto.of(totalCourses, totalStudents, totalRevenue);
    }

    /**
     * 사용자가 수강 중인 강의 목록 조회
     * - Enrollment 기준 (ACTIVE 상태만)
     * - 제목순, 최근 수강신청 순 정렬 지원
     */
    public Page<EnrolledCourseResponse> getEnrolledCourses(Long userId, Pageable pageable) {
        log.info("Getting enrolled courses for user ID: {}", userId);

        return enrollmentRepository.findEnrolledCourses(userId, EnrollmentStatus.ACTIVE, pageable);
    }

    /**
     * 강의 수정
     */
    @Transactional
    public Long updateCourse(Long courseId, CourseUpdateReqDto reqDto, Long userId) {
        log.info("Updating course with ID: {}", courseId);

        Course course = getCourseById(courseId);
        validateCourseOwner(course, userId);

        course.updateCourse(
                reqDto.getTitle(),
                reqDto.getDescription(),
                reqDto.getDetailedDescription(),
                reqDto.getThumbnail(),
                reqDto.getCategory(),
                reqDto.getLevel(),
                reqDto.getLanguage(),
                reqDto.getPrice(),
                reqDto.getFeatures()
        );

        return course.getId();
    }

    /**
     * 강의 삭제
     */
    @Transactional
    public void deleteCourse(Long courseId, Long userId) {
        log.info("Deleting course with ID: {}", courseId);

        Course course = getCourseById(courseId);
        validateCourseOwner(course, userId);

        courseRepository.delete(course);
    }

    private Course getCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new CustomException(ErrorCode.COURSE_NOT_FOUND));
    }

    private void validateCourseOwner(Course course, Long userId) {
        if (!course.isOwner(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }
}
