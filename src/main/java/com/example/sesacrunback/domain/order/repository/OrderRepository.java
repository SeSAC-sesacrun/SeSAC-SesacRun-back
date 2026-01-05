package com.example.sesacrunback.domain.order.repository;

import com.example.sesacrunback.domain.course.course.entity.Course;
import com.example.sesacrunback.domain.order.entity.Order;
import com.example.sesacrunback.domain.order.entity.OrderState;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderNumber(String orderNumber);

    /**
     * 유저가 해당 코스를 결제 완료(COMPLETED) 상태로 구매했는지 여부
     */
    boolean existsByUserIdAndOrderItems_Course_IdAndStatus(
            Long userId,
            Long courseId,
            OrderState status
    );

    /** 수강 권한 판단 단일 진입점 */
    default boolean hasCompletedOrderForCourse(Long userId, Long courseId) {
        return existsByUserIdAndOrderItems_Course_IdAndStatus(
                userId,
                courseId,
                OrderState.COMPLETED
        );
    }

    boolean existsByUserId(Long id);

    List<Order> findAllByUserId(Long id);

    /**
     * 결제 완료된 주문을 기준으로
     * 사용자가 수강 중인 강의 목록 조회
     */
    @Query(
            value = """
                SELECT DISTINCT oi.course
                FROM Order o
                JOIN o.orderItems oi
                LEFT JOIN FETCH oi.course.instructor
                WHERE o.user.id = :userId
                  AND o.status = 'COMPLETED'
            """,
            countQuery = """
                SELECT COUNT(DISTINCT oi.course.id)
                FROM Order o
                JOIN o.orderItems oi
                WHERE o.user.id = :userId
                  AND o.status = 'COMPLETED'
            """
    )
    Page<Course> findEnrolledCourses(
            @Param("userId") Long userId,
            Pageable pageable
    );
}
