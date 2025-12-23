package com.example.sesacrunback.domain.recruitment.post.controller;

import com.example.sesacrunback.domain.recruitment.post.dto.request.RecruitmentPostCreateReqDto;
import com.example.sesacrunback.domain.recruitment.post.dto.response.PostDetailResDto;
import com.example.sesacrunback.domain.recruitment.post.service.RecruitmentPostService;
import com.example.sesacrunback.global.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/community/posts")
@RequiredArgsConstructor
public class RecruitmentPostController {

    private final RecruitmentPostService recruitmentPostService;

    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createPost(
        @Valid @RequestBody RecruitmentPostCreateReqDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(recruitmentPostService.createPost(request, 2L)));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostDetailResDto>> viewPost(@PathVariable Long postId) {
        return ResponseEntity.ok(ApiResponse.success(recruitmentPostService.viewPost(postId)));
    }


}
