package com.example.sesacrunback.domain.order.controller;

import com.example.sesacrunback.domain.order.dto.response.OrderResponse;
import com.example.sesacrunback.domain.order.service.OrderService;
import com.example.sesacrunback.domain.user.entity.User;
import com.example.sesacrunback.global.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(
            @RequestHeader("X-USER-ID") Long userId // @AuthenticationPrincipal 대신 @RequestHeader 사용   // TODO : user 생성 후 변경
            //@AuthenticationPrincipal CustomUserDetails userDetails,
    ) {

        //User user = userDetails.getUser(); // 인증 정보에서 사용자 객체 가져오기
        //OrderResponse response = orderService.create(user);
        OrderResponse response = orderService.create(userId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
