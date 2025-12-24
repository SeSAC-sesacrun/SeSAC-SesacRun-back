package com.example.sesacrunback.domain.order.service;

import com.example.sesacrunback.domain.cart.entity.CartItem;
import com.example.sesacrunback.domain.cart.repository.CartRepository;
import com.example.sesacrunback.domain.order.dto.response.OrderResponse;
import com.example.sesacrunback.domain.order.entity.Order;
import com.example.sesacrunback.domain.order.repository.OrderRepository;
import com.example.sesacrunback.domain.user.entity.User;
import com.example.sesacrunback.domain.user.repository.UserRepository;
import com.example.sesacrunback.global.exception.CustomException;
import com.example.sesacrunback.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    @Transactional
    //public OrderResponse create(User user) { // TODO : CustomUserDetails 생성후 변경
    public OrderResponse create(Long userId) {
        // 1. 사용자 및 장바구니 아이템 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        List<CartItem> cartItems = cartRepository.findAllByUser(user);

        // 2. 장바구니 비어있으면 예외 처리
        if (cartItems.isEmpty()) {
            throw new CustomException(ErrorCode.CART_ITEM_NOT_FOUND);
        }

        // 3. Order.createOrder 정적 메서드를 사용하여 주문 생성 (로직 변경)
        Order order = Order.createOrder(user, cartItems);

        // 4. 주문 저장
        Order savedOrder = orderRepository.save(order);

        // 5. 주문 완료 후 장바구니 비우기
        cartRepository.deleteAll(cartItems);

        // 6. 응답 반환
        return OrderResponse.from(savedOrder);
    }
}
