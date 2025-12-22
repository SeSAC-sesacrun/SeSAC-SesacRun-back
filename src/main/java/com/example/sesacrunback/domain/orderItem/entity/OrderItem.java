package com.example.sesacrunback.domain.orderItem.entity;

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
}
