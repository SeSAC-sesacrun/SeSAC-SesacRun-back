package com.example.sesacrunback.domain.order.controller;

import com.example.sesacrunback.domain.order.dto.response.OrderDetailResponse;
import com.example.sesacrunback.domain.order.dto.response.PurchaseOrderResponse;
import com.example.sesacrunback.domain.order.service.OrderService;
import com.example.sesacrunback.global.common.dto.ApiResponse;
import com.example.sesacrunback.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/purchases")
    public ResponseEntity<ApiResponse<List<PurchaseOrderResponse>>> getMyPurchases(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getId();

        List<PurchaseOrderResponse> response = orderService.getPurchaseHistory(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderDetailResponse>> getOrderDetail(
            @PathVariable("orderId") Long orderId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getId();

        OrderDetailResponse response = orderService.getOrderDetail(orderId, userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
