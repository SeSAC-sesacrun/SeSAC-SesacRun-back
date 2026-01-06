package com.example.sesacrunback.domain.payment.repository;

import com.example.sesacrunback.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    boolean existsByPortonePaymentId(String impUid);

    /**
     * 강의별 총 수익 계산
     * - OrderItem.price 기준 (주문 당시 가격)
     * - Order.status = COMPLETED 인 주문만 집계
     * - 환불된 주문은 자동 제외됨
     */
    @Query("""
        SELECT COALESCE(SUM(oi.price), 0)
        FROM Order o
        JOIN o.orderItems oi
        WHERE oi.course.id = :courseId
          AND o.status = 'COMPLETED'
    """)
    Long calculateTotalRevenueByCourse(@Param("courseId") Long courseId);

    /**
     * 강사별 총 수익 계산
     * - OrderItem.price 기준 (주문 당시 가격)
     * - Order.status = COMPLETED 인 주문만 집계
     * - 환불된 주문은 자동 제외됨
     */
    @Query("""
        SELECT COALESCE(SUM(oi.price), 0)
        FROM Order o
        JOIN o.orderItems oi
        JOIN oi.course c
        WHERE c.instructor.id = :instructorId
          AND o.status = 'COMPLETED'
    """)
    Long calculateTotalRevenueByInstructor(@Param("instructorId") Long instructorId);
}
