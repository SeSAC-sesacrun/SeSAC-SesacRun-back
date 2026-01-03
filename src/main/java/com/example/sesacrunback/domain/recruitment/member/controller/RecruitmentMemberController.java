package com.example.sesacrunback.domain.recruitment.member.controller;

import com.example.sesacrunback.domain.recruitment.member.dto.request.MemberUpdateReqDto;
import com.example.sesacrunback.domain.recruitment.member.service.RecruitmentMemberService;
import com.example.sesacrunback.global.common.dto.ApiResponse;
import com.example.sesacrunback.global.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recruitments/posts/{postId}/members")
@RequiredArgsConstructor
public class RecruitmentMemberController {

    private final RecruitmentMemberService recruitmentMemberService;

    @PostMapping
    public ResponseEntity<ApiResponse<Long>> applyToRecruitment(@PathVariable Long postId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(
                recruitmentMemberService.applyToRecruitment(postId, userDetails.getId())));
    }

    @PatchMapping("/{memberId}")
    public ResponseEntity<ApiResponse<String>> updateMemberStatus(@PathVariable Long postId,
        @PathVariable Long memberId,
        @Valid @RequestBody MemberUpdateReqDto reqDto,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        recruitmentMemberService.updateMemberStatus(postId, memberId, userDetails.getId(), reqDto);
        return ResponseEntity.ok(ApiResponse.success("상태 변경에 성공했습니다."));
    }

}
