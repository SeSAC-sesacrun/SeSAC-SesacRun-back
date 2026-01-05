package com.example.sesacrunback.domain.payment.controller;

import com.example.sesacrunback.domain.payment.dto.request.PaymentCreateRequest;
import com.example.sesacrunback.domain.payment.dto.response.PaymentResponse;
import com.example.sesacrunback.domain.payment.service.PaymentService;
import com.example.sesacrunback.global.common.dto.ApiResponse;
import com.example.sesacrunback.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("")
    public ResponseEntity<ApiResponse<PaymentResponse>> complete(
            @RequestBody PaymentCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails

    ) {
        PaymentResponse response = paymentService.complete(request, userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
