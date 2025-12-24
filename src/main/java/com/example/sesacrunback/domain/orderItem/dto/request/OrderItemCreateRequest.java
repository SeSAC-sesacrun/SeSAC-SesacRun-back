package com.example.sesacrunback.domain.orderItem.dto.request;

import com.example.sesacrunback.domain.order.entity.Order;
import com.example.sesacrunback.domain.order.entity.OrderState;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class OrderItemCreateRequest {
    private Long orderId;
    private String orderNumber;
    private int totalAmount;
    private OrderState status;
    private LocalDateTime createdAt;

    @Builder
    private OrderItemCreateRequest(Long orderId, String orderNumber, int totalAmount, OrderState status, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static OrderItemCreateRequest from(Order order) {
        return OrderItemCreateRequest.builder()
                .orderId(order.getId())
                .orderNumber(order.getOrderNumber())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
