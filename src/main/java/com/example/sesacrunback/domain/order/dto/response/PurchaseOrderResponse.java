package com.example.sesacrunback.domain.order.dto.response;

import com.example.sesacrunback.domain.order.entity.Order;
import com.example.sesacrunback.domain.orderItem.entity.OrderItem;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PurchaseOrderResponse {
    private final Long orderId;
    private final String orderTitle;
    private final String thumbnail;
    private final int totalAmount;
    private final LocalDateTime purchasedAt;

    public static PurchaseOrderResponse from(Order order) {
        List<OrderItem> orderItems = order.getOrderItems();
        if (orderItems == null || orderItems.isEmpty()) {
            return PurchaseOrderResponse.builder()
                    .orderId(order.getId())
                    .orderTitle("주문 항목 없음")
                    .thumbnail(null) // 기본 이미지 URL
                    .totalAmount(order.getPayment() != null ? order.getPayment().getAmount() : 0)
                    .purchasedAt(order.getCreatedAt())
                    .build();
        }

        String title = generateOrderTitle(orderItems);
        String representativeThumbnail = orderItems.get(0).getCourse().getThumbnail();
        int paymentAmount = order.getPayment() != null ? order.getPayment().getAmount() : 0;

        return PurchaseOrderResponse.builder()
                .orderId(order.getId())
                .orderTitle(title)
                .thumbnail(representativeThumbnail)
                .totalAmount(paymentAmount)
                .purchasedAt(order.getCreatedAt())
                .build();
    }

    private static String generateOrderTitle(List<OrderItem> orderItems) {
        String firstItemName = orderItems.get(0).getCourseName();
        int remainingCount = orderItems.size() - 1;

        if (remainingCount > 0) {
            return firstItemName + " 외 " + remainingCount + "건";
        } else {
            return firstItemName;
        }
    }
}
