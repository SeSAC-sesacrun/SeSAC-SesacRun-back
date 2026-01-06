package com.example.sesacrunback.domain.refund.repository;

import com.example.sesacrunback.domain.payment.entity.Payment;
import com.example.sesacrunback.domain.refund.entity.Refund;
import com.example.sesacrunback.domain.refund.entity.RefundStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundRepository  extends JpaRepository<Refund, Long> {
    boolean existsByPaymentAndStatus(Payment payment, RefundStatus status);
}
