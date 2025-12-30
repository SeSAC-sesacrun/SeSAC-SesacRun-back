package com.example.sesacrunback.domain.recruitment.post.controller;

import com.example.sesacrunback.domain.recruitment.post.dto.request.RecruitmentPostCreateReqDto;
import com.example.sesacrunback.domain.recruitment.post.dto.request.RecruitmentPostUpdateReqDto;
import com.example.sesacrunback.domain.recruitment.post.dto.response.PostDetailResDto;
import com.example.sesacrunback.domain.recruitment.post.entity.RecruitmentCategory;
import com.example.sesacrunback.domain.recruitment.post.entity.RecruitmentStatus;
import com.example.sesacrunback.domain.recruitment.post.service.RecruitmentPostService;
import com.example.sesacrunback.global.common.dto.ApiResponse;
import com.example.sesacrunback.global.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recruitments/posts")
@RequiredArgsConstructor
public class RecruitmentPostController {

    private final RecruitmentPostService recruitmentPostService;

    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createPost(
        @Valid @RequestBody RecruitmentPostCreateReqDto request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(
                recruitmentPostService.createPost(request, userDetails.getId())));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostDetailResDto>> viewPost(@PathVariable Long postId) {
        return ResponseEntity.ok(ApiResponse.success(recruitmentPostService.viewPost(postId)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Slice<PostDetailResDto>>> viewPosts(
        @RequestParam RecruitmentCategory category,
        @RequestParam(required = false) RecruitmentStatus status,
        @PageableDefault Pageable pageable) {

        return ResponseEntity.ok(
            ApiResponse.success(recruitmentPostService.viewPosts(category, status, pageable)));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<ApiResponse<Long>> updatePost(@PathVariable Long postId,
        @Valid @RequestBody RecruitmentPostUpdateReqDto request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(
            ApiResponse.success(
                recruitmentPostService.updatePost(postId, request, userDetails.getId())));

    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<String>> deletePost(@PathVariable Long postId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        recruitmentPostService.deletePost(postId, userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success("삭제되었습니다."));
    }

}
