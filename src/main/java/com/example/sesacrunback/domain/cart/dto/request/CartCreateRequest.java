package com.example.sesacrunback.domain.cart.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartCreateRequest {
    @NotNull(message = "강의 ID는 필수입니다")
    private Long courseId;

}
