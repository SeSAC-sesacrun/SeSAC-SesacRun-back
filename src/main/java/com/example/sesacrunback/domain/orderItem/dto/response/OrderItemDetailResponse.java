package com.example.sesacrunback.domain.orderItem.dto.response;

import com.example.sesacrunback.domain.orderItem.entity.OrderItem;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class OrderItemDetailResponse {
    private final Long id;
    private final Long orderId;
    private final Long courseId;
    private final String courseName;
    private final int price;
    private final String thumbnail;
    private final LocalDateTime createdAt;

    public static OrderItemDetailResponse from(OrderItem orderItem) {
        return OrderItemDetailResponse.builder()
                .id(orderItem.getId())
                .orderId(orderItem.getOrder().getId())
                .courseId(orderItem.getCourse().getId())
                .courseName(orderItem.getCourseName())
                .price(orderItem.getPrice())
                .thumbnail(orderItem.getCourse().getThumbnail()) // Course 엔티티에서 썸네일 가져옴
                .createdAt(orderItem.getCreatedAt())
                .build();
    }
}
