package com.example.sesacrunback.domain.order.dto.response;

import com.example.sesacrunback.domain.order.entity.Order;
import com.example.sesacrunback.domain.order.entity.OrderState;
import com.example.sesacrunback.domain.orderItem.dto.response.OrderItemDetailResponse;
import com.example.sesacrunback.domain.payment.dto.response.PaymentDetailResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class OrderDetailResponse {
    private final Long id;
    private final String orderNumber;
    private final Long userId;
    private final int totalAmount;
    private final OrderState status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final List<OrderItemDetailResponse> orderItems;
    private final PaymentDetailResponse payment;

    public static OrderDetailResponse from(Order order, ObjectMapper objectMapper) {
        List<OrderItemDetailResponse> itemDetails = order.getOrderItems().stream()
                .map(OrderItemDetailResponse::from)
                .collect(Collectors.toList());

        PaymentDetailResponse paymentDetails = null;
        if (order.getPayment() != null) {
            paymentDetails = PaymentDetailResponse.from(order.getPayment(), objectMapper);
        }

        return OrderDetailResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .userId(order.getUser().getId())
                .totalAmount(order.getTotalAmount()) // Order 엔티티의 totalAmount 사용
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .orderItems(itemDetails)
                .payment(paymentDetails)
                .build();
    }
}
