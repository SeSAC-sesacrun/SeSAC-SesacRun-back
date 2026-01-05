package com.example.sesacrunback.domain.order.service;

import com.example.sesacrunback.domain.order.dto.response.OrderDetailResponse;
import com.example.sesacrunback.domain.order.dto.response.PurchaseOrderResponse;
import com.example.sesacrunback.domain.order.entity.OrderState;
import com.example.sesacrunback.domain.order.repository.OrderRepository;
import com.example.sesacrunback.domain.user.entity.User;
import com.example.sesacrunback.domain.user.repository.UserRepository;
import com.example.sesacrunback.global.exception.CustomException;
import com.example.sesacrunback.global.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.sesacrunback.domain.order.entity.Order;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;

    public List<PurchaseOrderResponse> getPurchaseHistory(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 사용자의 완료된 주문 목록을 조회
        List<Order> completedOrders = orderRepository.findAllByUserAndStatusOrderByCreatedAtDesc(user, OrderState.COMPLETED);

        // Order 목록을 PurchaseOrderResponse 목록으로 변환
        return completedOrders.stream()
                .map(PurchaseOrderResponse::from)
                .collect(Collectors.toList());
    }

    public OrderDetailResponse getOrderDetail(Long orderId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Order order = orderRepository.findByIdAndUser(orderId, user)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        return OrderDetailResponse.from(order, objectMapper);
    }
}
