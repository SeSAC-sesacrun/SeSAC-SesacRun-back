package com.example.sesacrunback.domain.payment.entity;

import com.example.sesacrunback.domain.order.entity.Order;
import com.example.sesacrunback.domain.refund.entity.Refund;
import com.example.sesacrunback.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "payments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

    @Column(nullable = false, unique = true)
    private String portonePaymentId; // PortOne의 imp_uid

    @Column(nullable = false)
    private int amount; // 결제 금액

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status; // 결제 상태

    @Lob
    @Column(columnDefinition = "TEXT")
    private String portoneData; // 포트원 결제 데이터 (JSON)

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order; // 관련된 주문

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL)
    private List<Refund> refunds = new ArrayList<>(); // 이 결제와 관련된 환불 목록

    @Builder
    private Payment(String portonePaymentId, int amount, PaymentStatus status, String portoneData, Order order) {
        this.portonePaymentId = portonePaymentId;
        this.amount = amount;
        this.status = status;
        this.portoneData = portoneData;
        this.order = order;
    }

    // 환불시 주문 상태 변경
    public void cancel() {
        this.status = PaymentStatus.REFUND;
    }

}
