package com.example.sesacrunback.domain.refund.entity;

import com.example.sesacrunback.domain.payment.entity.Payment;
import com.example.sesacrunback.domain.payment.entity.PaymentStatus;
import com.example.sesacrunback.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "refunds")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Refund extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment; // 환불된 결제

    @Column(nullable = false)
    private int amount; // 환불 금액

    private String reason; // 환불 사유

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RefundStatus status; // 환불 상태

    @Builder
    public Refund(Payment payment, int amount, String reason, RefundStatus status) {
        this.payment = payment;
        this.amount = amount;
        this.reason = reason;
        this.status = status;
    }

    public void complete() {
        this.status = RefundStatus.COMPLETED;
    }

    public void fail() {
        this.status = RefundStatus.FAILED;
    }
}