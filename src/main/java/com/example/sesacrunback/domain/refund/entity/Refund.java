package com.example.sesacrunback.domain.refund.entity;

import com.example.sesacrunback.domain.payment.entity.Payment;
import com.example.sesacrunback.global.common.entity.BaseCreateEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "refunds")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Refund extends BaseCreateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

    @Column(nullable = false)
    private String refundId; // 포트원 환불 ID

    @Column(nullable = false)
    private int refundAmount; // 환불 금액

    private String reason; // 환불 사유

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RefundStatus status; // 환불 상태

    @ManyToOne
    @JoinColumn(name = "payment_id",nullable = false)
    private Payment payment; // 관련된 원 결제
}
