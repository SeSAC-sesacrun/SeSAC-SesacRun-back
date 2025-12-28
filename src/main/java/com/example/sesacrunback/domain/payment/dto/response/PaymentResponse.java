package com.example.sesacrunback.domain.payment.dto.response;

import com.example.sesacrunback.domain.payment.entity.Payment;
import com.example.sesacrunback.domain.payment.entity.PaymentStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PaymentResponse {
    private final Long paymentId;
    private final Long orderId;
    private final int amount;
    private final PaymentStatus status;
    private final LocalDateTime paidAt;

    @Builder
    private PaymentResponse(Long paymentId, Long orderId, int amount, PaymentStatus status, LocalDateTime paidAt) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
        this.status = status;
        this.paidAt = paidAt;
    }

    public static PaymentResponse from(Payment payment) {
        return PaymentResponse.builder()
                .paymentId(payment.getId())
                .orderId(payment.getOrder().getId())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .paidAt(payment.getCreatedAt())
                .build();
    }
}
