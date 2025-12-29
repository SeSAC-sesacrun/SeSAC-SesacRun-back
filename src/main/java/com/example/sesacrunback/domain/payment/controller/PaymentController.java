package com.example.sesacrunback.domain.payment.controller;

import com.example.sesacrunback.domain.payment.dto.request.PaymentCreateRequest;
import com.example.sesacrunback.domain.payment.dto.response.PaymentResponse;
import com.example.sesacrunback.domain.payment.service.PaymentService;
import com.example.sesacrunback.global.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("")
    public ResponseEntity<ApiResponse<PaymentResponse>> complete(
            @RequestBody PaymentCreateRequest request,
            // @AuthenticationPrincipal CustomUserDetails userDetails
            @RequestHeader("X-USER-ID") Long userId // @AuthenticationPrincipal 대신 @RequestHeader 사용   // TODO : user 생성 후 변경

    ) {
        // Long userId = userDetails.getUser().getId(); // TODO : 실제 userId 가져오기

        PaymentResponse response = paymentService.complete(request, userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
