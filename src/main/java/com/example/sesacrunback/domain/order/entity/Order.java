package com.example.sesacrunback.domain.order.entity;

import com.example.sesacrunback.domain.cart.entity.CartItem;
import com.example.sesacrunback.domain.orderItem.entity.OrderItem;
import com.example.sesacrunback.domain.payment.entity.Payment;
import com.example.sesacrunback.domain.user.entity.User;
import com.example.sesacrunback.global.common.entity.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

    @Column(nullable = false)
    private String orderNumber; // 주문 번호

    @Column(nullable = false)
    private int totalAmount; // 총 주문 금액

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderState status; // 주문 상태

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user; // 주문한 사용자

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>(); // 주문에 포함된 항목 목록

    @OneToOne(mappedBy = "order")
    private Payment payment; // 주문에 대한 결제 정보

    @Builder
    public Order(String orderNumber, User user, Integer totalAmount, OrderState status) {
        this.orderNumber = orderNumber;
        this.user = user;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public static Order createOrder(User user, List<CartItem> cartItems, String merchantUid) {
        Order order = Order.builder()
                .user(user)
                .orderNumber(merchantUid)
                .totalAmount(
                        cartItems.stream()
                                .mapToInt(item -> item.getCourse().getPrice())
                                .sum()
                )
                .status(OrderState.CREATED)
                .build();

        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> OrderItem.fromCartItem(cartItem, order))
                .collect(Collectors.toList());

        order.orderItems.addAll(orderItems);
        return order;
    }

    // 결제 완료 시 주문 상태 변경
    public void completePayment() {
        this.status = OrderState.COMPLETED;;
    }
}
