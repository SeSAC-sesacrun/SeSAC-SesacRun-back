package com.example.sesacrunback.domain.course.lecture.controller;

import com.example.sesacrunback.domain.course.lecture.dto.request.LectureCreateReqDto;
import com.example.sesacrunback.domain.course.lecture.dto.request.LectureUpdateReqDto;
import com.example.sesacrunback.domain.course.lecture.dto.response.LectureResponse;
import com.example.sesacrunback.domain.course.lecture.service.LectureService;
import com.example.sesacrunback.global.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sections/{sectionId}/lectures")
@RequiredArgsConstructor
public class LectureController {

    private final LectureService lectureService;

    /* ========= 강의 생성 ========= */
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createLecture(
            @PathVariable Long sectionId,
            @Valid @RequestBody LectureCreateReqDto request
    ) {
        Long userId = 1L; // TODO: @AuthenticationPrincipal

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        lectureService.createLecture(request, sectionId, userId)
                ));
    }

    /* ========= 강의 단건 조회 ========= */
    @GetMapping("/{lectureId}")
    public ResponseEntity<ApiResponse<LectureResponse>> viewLecture(
            @PathVariable Long lectureId
    ) {
        Long userId = 1L;

        return ResponseEntity.ok(
                ApiResponse.success(
                        lectureService.viewLecture(lectureId, userId)
                )
        );
    }

    /* ========= 강의 수정 ========= */
    @PutMapping("/{lectureId}")
    public ResponseEntity<ApiResponse<Long>> updateLecture(
            @PathVariable Long lectureId,
            @Valid @RequestBody LectureUpdateReqDto request
    ) {
        Long userId = 1L;

        return ResponseEntity.ok(
                ApiResponse.success(
                        lectureService.updateLecture(lectureId, request, userId)
                )
        );
    }

    /* ========= 강의 삭제 ========= */
    @DeleteMapping("/{lectureId}")
    public ResponseEntity<ApiResponse<String>> deleteLecture(
            @PathVariable Long lectureId
    ) {
        Long userId = 1L;

        lectureService.deleteLecture(lectureId, userId);
        return ResponseEntity.ok(ApiResponse.success("삭제되었습니다."));
    }
}