package com.example.sesacrunback.domain.refund.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefundResponse {
    private final Long refundId;
    private final Long orderId;
    private final String status;
    private final String message;
}
