package com.example.sesacrunback.domain.order.entity;

public enum OrderState {
    ORDER,
    CREATED,   // 주문 생성됨 (결제 대기)
    COMPLETED, // 결제 완료됨
    REFUND,     // 환불
}
