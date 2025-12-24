package com.example.sesacrunback.domain.recruitment.member.controller;

import com.example.sesacrunback.domain.recruitment.member.service.RecruitmentMemberService;
import com.example.sesacrunback.global.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recruitments/{postId}/members")
@RequiredArgsConstructor
public class RecruitmentMemberController {

    private final RecruitmentMemberService recruitmentMemberService;

    @PostMapping
    public ResponseEntity<ApiResponse<Long>> applyToRecruitment(@PathVariable Long postId) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(recruitmentMemberService.applyToRecruitment(postId, 1L)));
    }

}
