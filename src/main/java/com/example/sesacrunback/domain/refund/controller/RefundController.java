package com.example.sesacrunback.domain.refund.controller;

import com.example.sesacrunback.domain.refund.dto.request.RefundRequest;
import com.example.sesacrunback.domain.refund.dto.response.RefundResponse;
import com.example.sesacrunback.domain.refund.service.RefundService;
import com.example.sesacrunback.global.common.dto.ApiResponse;
import com.example.sesacrunback.global.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/refunds")
@RequiredArgsConstructor
public class RefundController {
    private final RefundService refundService;

    @PostMapping
    public ResponseEntity<ApiResponse<RefundResponse>> createRefund(@Valid @RequestBody RefundRequest request,
                                                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        RefundResponse response = refundService.createRefund(request, userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
