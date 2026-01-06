package com.example.sesacrunback.domain.refund.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RefundRequest {
    @NotNull(message = "주문 ID는 필수입니다.")
    private Long orderId;
    private String reason;
}
