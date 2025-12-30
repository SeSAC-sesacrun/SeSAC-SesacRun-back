package com.example.sesacrunback.domain.payment.repository;

import com.example.sesacrunback.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    boolean existsByPortonePaymentId(String impUid);
}
