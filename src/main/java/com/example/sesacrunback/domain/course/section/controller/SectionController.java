package com.example.sesacrunback.domain.course.section.controller;

import com.example.sesacrunback.domain.course.section.dto.request.SectionCreateReqDto;
import com.example.sesacrunback.domain.course.section.dto.request.SectionUpdateReqDto;
import com.example.sesacrunback.domain.course.section.dto.response.SectionResponse;
import com.example.sesacrunback.domain.course.section.service.SectionService;
import com.example.sesacrunback.global.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/courses/{courseId}/sections")
@RequiredArgsConstructor
public class SectionController {

    private final SectionService sectionService;

    /* ========= 섹션 생성 ========= */
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createSection(
            @PathVariable Long courseId,
            @Valid @RequestBody SectionCreateReqDto request
    ) {
        Long userId = 1L; // TODO: @AuthenticationPrincipal

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        sectionService.createSection(courseId, request, userId)
                ));
    }

    /* ========= 섹션 단건 조회 ========= */
    @GetMapping("/{sectionId}")
    public ResponseEntity<ApiResponse<SectionResponse>> viewSection(
            @PathVariable Long sectionId
    ) {
        Long userId = 1L;

        return ResponseEntity.ok(
                ApiResponse.success(
                        sectionService.viewSection(sectionId, userId)
                )
        );
    }

    /* ========= 섹션 수정 ========= */
    @PutMapping("/{sectionId}")
    public ResponseEntity<ApiResponse<Long>> updateSection(
            @PathVariable Long sectionId,
            @Valid @RequestBody SectionUpdateReqDto request
    ) {
        Long userId = 1L;

        return ResponseEntity.ok(
                ApiResponse.success(
                        sectionService.updateSection(sectionId, request, userId)
                )
        );
    }

    /* ========= 섹션 삭제 ========= */
    @DeleteMapping("/{sectionId}")
    public ResponseEntity<ApiResponse<String>> deleteSection(
            @PathVariable Long sectionId
    ) {
        Long userId = 1L;

        sectionService.deleteSection(sectionId, userId);
        return ResponseEntity.ok(ApiResponse.success("삭제되었습니다."));
    }
}
