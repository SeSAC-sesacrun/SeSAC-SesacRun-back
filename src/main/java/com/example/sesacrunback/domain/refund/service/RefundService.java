package com.example.sesacrunback.domain.refund.service;

import com.example.sesacrunback.domain.order.entity.Order;
import com.example.sesacrunback.domain.order.entity.OrderState;
import com.example.sesacrunback.domain.order.repository.OrderRepository;
import com.example.sesacrunback.domain.payment.entity.Payment;
import com.example.sesacrunback.domain.payment.entity.PaymentStatus;
import com.example.sesacrunback.domain.refund.dto.request.RefundRequest;
import com.example.sesacrunback.domain.refund.dto.response.RefundResponse;
import com.example.sesacrunback.domain.refund.entity.Refund;
import com.example.sesacrunback.domain.refund.entity.RefundStatus;
import com.example.sesacrunback.domain.refund.repository.RefundRepository;
import com.example.sesacrunback.domain.user.entity.User;
import com.example.sesacrunback.domain.user.repository.UserRepository;
import com.example.sesacrunback.global.exception.CustomException;
import com.example.sesacrunback.global.exception.ErrorCode;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class RefundService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final RefundRepository refundRepository;
    private final IamportClient iamportClient;

    public RefundResponse createRefund(RefundRequest request, Long userId) {
        // 1. 주문 및 사용자 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        Order order = orderRepository.findByIdAndUser(request.getOrderId(), user)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
        Payment payment = order.getPayment();
        if (payment == null) {
            throw new CustomException(ErrorCode.PAYMENT_NOT_FOUND);
        }

        // 2. 주문/결제 상태 및 중복 환불 요청 검증
        if (order.getStatus() == OrderState.REFUND || payment.getStatus() == PaymentStatus.REFUND) {
            throw new CustomException(ErrorCode.ORDER_ALREADY_CANCELED);
        }
        if (refundRepository.existsByPaymentAndStatus(payment, RefundStatus.REQUESTED) ||
            refundRepository.existsByPaymentAndStatus(payment, RefundStatus.COMPLETED)) {
            throw new CustomException(ErrorCode.REFUND_ALREADY_PROCESSED);
        }

        // 3. 환불 엔티티 생성 및 'REQUESTED' 상태로 저장
        Refund refund = Refund.builder()
                .payment(payment)
                .reason(request.getReason())
                .amount(payment.getAmount())
                .status(RefundStatus.REQUESTED)
                .build();
        refundRepository.save(refund);

        // 4. 외부 API 호출 및 결과에 따른 상태 업데이트
        try {
            // 0원 강의 처리
            if (payment.getAmount() == 0) {
                order.cancel();
                payment.cancel();
                refund.complete();
                return RefundResponse.builder()
                        .refundId(refund.getId())
                        .orderId(order.getId())
                        .status(refund.getStatus().name())
                        .message("무료 강의 취소가 성공적으로 처리되었습니다.")
                        .build();
            }

            // 포트원 환불 처리
            String impUid = payment.getPortonePaymentId();
            BigDecimal refundAmount = new BigDecimal(payment.getAmount());
            CancelData cancelData = new CancelData(impUid, true, refundAmount);
            cancelData.setReason(request.getReason());

            iamportClient.cancelPaymentByImpUid(cancelData);

            // 성공 시 DB 상태 변경
            order.cancel();
            payment.cancel();
            refund.complete();

            return RefundResponse.builder()
                    .refundId(refund.getId())
                    .orderId(order.getId())
                    .status(refund.getStatus().name())
                    .message("환불 요청이 성공적으로 처리되었습니다.")
                    .build();

        } catch (IamportResponseException e) {
            // 외부 API 실패
            refund.fail();
            throw new CustomException(ErrorCode.REFUND_FAILED);
        } catch (IOException e) {
            // 통신 실패
            refund.fail();
            throw new CustomException(ErrorCode.REFUND_FAILED);
        } catch (Exception e) {
            // 기타 예외 발생
            refund.fail();
            throw new CustomException(ErrorCode.INTERNAL_ERROR);
        }
    }
}