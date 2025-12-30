package com.example.sesacrunback.domain.order.repository;

import com.example.sesacrunback.domain.order.entity.Order;
import com.example.sesacrunback.domain.order.entity.OrderState;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderNumber(String orderNumber);

    /**
     * 유저가 해당 코스를 결제 완료(COMPLETED) 상태로 구매했는지 여부
     */
    boolean existsByUserIdAndOrderItems_CourseIdAndStatus(
            Long userId,
            Long courseId,
            OrderState status
    );

    /** 수강 권한 판단 단일 진입점 */
    default boolean hasCompletedOrderForCourse(Long userId, Long courseId) {
        return existsByUserIdAndOrderItems_CourseIdAndStatus(
                userId,
                courseId,
                OrderState.COMPLETED
        );
    }

}
