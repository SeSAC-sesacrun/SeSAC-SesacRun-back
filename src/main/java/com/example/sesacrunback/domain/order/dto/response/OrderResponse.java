package com.example.sesacrunback.domain.order.dto.response;

import com.example.sesacrunback.domain.order.entity.Order;
import com.example.sesacrunback.domain.order.entity.OrderState;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OrderResponse {
    private final Long orderId;
    private final String orderNumber;
    private final int totalAmount;
    private final OrderState status;
    private final LocalDateTime createdAt;

    @Builder
    private OrderResponse(Long orderId, String orderNumber, int totalAmount, OrderState status, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static OrderResponse from(Order order) {
        return OrderResponse.builder()
                .orderId(order.getId())
                .orderNumber(order.getOrderNumber())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
