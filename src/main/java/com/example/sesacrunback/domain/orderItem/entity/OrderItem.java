package com.example.sesacrunback.domain.orderItem.entity;

import com.example.sesacrunback.domain.cart.entity.CartItem;
import com.example.sesacrunback.domain.course.course.entity.Course;
import com.example.sesacrunback.domain.order.entity.Order;
import com.example.sesacrunback.global.common.entity.BaseCreateEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "order_items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem extends BaseCreateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

    @Column(nullable = false)
    private String courseName; // 주문 당시의 강의 이름 (기록용)

    @Column(nullable = false)
    private  int price; // 주문 당시의 가격 (기록용)

    @ManyToOne
    @JoinColumn(name = "order_id",nullable = false)
    private Order order; // 소속된 주문

    @ManyToOne
    @JoinColumn(name = "course_id",nullable = false)
    private Course course; // 주문한 강의

    @Builder
    public OrderItem(Order order, Course course, String courseName, Integer price) {
        this.order = order;
        this.course = course;
        this.courseName = courseName;
        this.price = price;
    }

    public static OrderItem fromCartItem(CartItem cartItem, Order order) {
        return OrderItem.builder()
                .course(cartItem.getCourse())
                .courseName(cartItem.getCourse().getTitle())
                .price(cartItem.getCourse().getPrice())
                .order(order)
                .build();
    }
}
