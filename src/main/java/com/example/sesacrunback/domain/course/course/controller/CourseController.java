package com.example.sesacrunback.domain.course.course.controller;

import com.example.sesacrunback.domain.course.course.dto.request.CourseUpdateReqDto;
import com.example.sesacrunback.domain.course.course.dto.request.CreateCourseRequest;
import com.example.sesacrunback.domain.course.course.dto.response.CourseDetailResponse;
import com.example.sesacrunback.domain.course.course.dto.response.CourseResponse;
import com.example.sesacrunback.domain.course.course.dto.response.CourseWatchResponse;
import com.example.sesacrunback.domain.course.course.service.CourseService;
import com.example.sesacrunback.global.common.dto.ApiResponse;
import com.example.sesacrunback.global.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CourseResponse>>> getAllCourses(
            @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<CourseResponse> courses = courseService.getAllCourses(pageable);
        return ResponseEntity.ok(ApiResponse.success(courses));
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<ApiResponse<CourseDetailResponse>> getCourseDetail(
            @PathVariable Long courseId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails != null ? userDetails.getId() : null;
        CourseDetailResponse course = courseService.getCourseDetail(courseId, userId);
        return ResponseEntity.ok(ApiResponse.success(course));
    }

    @GetMapping("/{courseId}/watch")
    public ResponseEntity<ApiResponse<CourseWatchResponse>> watchCourse(
            @PathVariable Long courseId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        CourseWatchResponse course = courseService.getCourseForWatch(courseId, userId);
        return ResponseEntity.ok(ApiResponse.success(course));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<Page<CourseResponse>>> getCoursesByCategory(
            @PathVariable String category,
            @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<CourseResponse> courses = courseService.getCoursesByCategory(category, pageable);
        return ResponseEntity.ok(ApiResponse.success(courses));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<CourseResponse>>> searchCourses(
            @RequestParam String keyword,
            @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<CourseResponse> courses = courseService.searchCourses(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(courses));
    }

    @GetMapping("/popular")
    public ResponseEntity<ApiResponse<Page<CourseResponse>>> getPopularCourses(
            @PageableDefault(size = 12) Pageable pageable) {
        Page<CourseResponse> courses = courseService.getPopularCourses(pageable);
        return ResponseEntity.ok(ApiResponse.success(courses));
    }

    @PostMapping
    // @PreAuthorize("hasRole('INSTRUCTOR')") // 인증 시스템 협업 중이므로 주석 처리
    public ResponseEntity<ApiResponse<CourseResponse>> createCourse(
            @Valid @RequestBody CreateCourseRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long instructorId = userDetails.getId();
        CourseResponse course = courseService.createCourse(request, instructorId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(course));
    }

    @GetMapping("/my")
    // @PreAuthorize("hasRole('INSTRUCTOR')") // 인증 시스템 협업 중이므로 주석 처리
    public ResponseEntity<ApiResponse<Page<CourseResponse>>> getMyCourses(
            @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long instructorId = userDetails.getId();
        Page<CourseResponse> courses = courseService.getMyCourses(pageable, instructorId);
        return ResponseEntity.ok(ApiResponse.success(courses));
    }

    @PutMapping("/{courseId}")
    // @PreAuthorize("hasRole('INSTRUCTOR')") // 인증 시스템 협업 중이므로 주석 처리
    public ResponseEntity<ApiResponse<Long>> updateCourse(
            @PathVariable Long courseId,
            @Valid @RequestBody CourseUpdateReqDto request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        Long updatedCourseId = courseService.updateCourse(courseId, request, userId);
        return ResponseEntity.ok(ApiResponse.success(updatedCourseId));
    }

    @DeleteMapping("/{courseId}")
    // @PreAuthorize("hasRole('INSTRUCTOR')") // 인증 시스템 협업 중이므로 주석 처리
    public ResponseEntity<ApiResponse<Void>> deleteCourse(
            @PathVariable Long courseId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        courseService.deleteCourse(courseId, userId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
