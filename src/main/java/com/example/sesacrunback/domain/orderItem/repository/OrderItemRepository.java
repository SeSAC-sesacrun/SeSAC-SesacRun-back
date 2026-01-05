package com.example.sesacrunback.domain.orderItem.repository;

import com.example.sesacrunback.domain.order.entity.OrderState;
import com.example.sesacrunback.domain.orderItem.entity.OrderItem;
import com.example.sesacrunback.domain.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    boolean existsByOrderUserAndCourseIdAndOrderStatus(User user, Long courseId, OrderState status);

    @EntityGraph(attributePaths = {"order", "course"})
    List<OrderItem> findAllByOrderUserAndOrderStatusOrderByOrderCreatedAtDesc(User user, OrderState orderStatus);
}
