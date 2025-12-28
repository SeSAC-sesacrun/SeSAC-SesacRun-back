package com.example.sesacrunback.domain.payment.service;

import com.example.sesacrunback.domain.cart.entity.CartItem;
import com.example.sesacrunback.domain.cart.repository.CartRepository;
import com.example.sesacrunback.domain.order.entity.Order;
import com.example.sesacrunback.domain.order.repository.OrderRepository;
import com.example.sesacrunback.domain.payment.dto.request.PaymentCreateRequest;
import com.example.sesacrunback.domain.payment.dto.response.PaymentResponse;
import com.example.sesacrunback.domain.payment.entity.Payment;
import com.example.sesacrunback.domain.payment.entity.PaymentStatus;
import com.example.sesacrunback.domain.payment.repository.PaymentRepository;
import com.example.sesacrunback.domain.user.entity.User;
import com.example.sesacrunback.domain.user.repository.UserRepository;
import com.example.sesacrunback.global.exception.CustomException;
import com.example.sesacrunback.global.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    @Transactional
    public PaymentResponse complete(PaymentCreateRequest request, Long userId) throws IOException {
        // 1. 사용자 정보 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 2. imp_uid로 이미 처리된 결제인지 확인 (중복 저장 방지)
        if (paymentRepository.existsByPortonePaymentId(request.getImpUid())) {
            throw new CustomException(ErrorCode.PAYMENT_ALREADY_EXISTS);
        }

        // 3. 사용자의 장바구니 아이템 조회
        List<CartItem> cartItems = cartRepository.findAllByUser(user);
        if (cartItems.isEmpty()) {
            // 결제할 아이템이 장바구니에 없는 경우 예외 처리 (PortOne 웹훅과 동시 접근 등 비정상 상황 방지)
            throw new CustomException(ErrorCode.CART_ITEM_NOT_FOUND);
        }

        // 4. 금액 검증 (DB의 장바구니 총액과 실제 결제 금액 비교)
        int totalAmountFromCart = cartItems.stream()
                .mapToInt(item -> item.getCourse().getPrice())
                .sum();

        if (totalAmountFromCart != request.getPaidAmount()) {
            throw new CustomException(ErrorCode.PAYMENT_AMOUNT_MISMATCH);
        }

        // 5. Order 엔티티 생성 (OrderItem 포함)
        Order order = Order.createOrder(user, cartItems, request.getMerchantUid());

        // 6. Payment 엔티티 생성
        Payment payment = Payment.builder()
                .portonePaymentId(request.getImpUid())
                .order(order)
                .amount(request.getPaidAmount())
                .status(PaymentStatus.fromStatusString(request.getStatus()))
                .portoneData(objectMapper.writeValueAsString(request))
                .build();

        // 7. Order 및 Payment 저장 (Order에 Cascade 설정으로 OrderItem도 함께 저장됨)
        orderRepository.save(order);
        Payment savedPayment = paymentRepository.save(payment);

        // 8. 주문 상태 변경
        order.completePayment();

        // 9. 장바구니 비우기
        cartRepository.deleteAll(cartItems);

        return PaymentResponse.from(savedPayment);
    }

}
