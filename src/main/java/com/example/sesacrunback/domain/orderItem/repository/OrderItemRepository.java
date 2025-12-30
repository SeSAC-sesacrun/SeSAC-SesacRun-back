package com.example.sesacrunback.domain.orderItem.repository;

import com.example.sesacrunback.domain.order.entity.OrderState;
import com.example.sesacrunback.domain.orderItem.entity.OrderItem;
import com.example.sesacrunback.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    boolean existsByOrderUserAndCourseIdAndOrderStatus(User user, Long courseId, OrderState status);
}
