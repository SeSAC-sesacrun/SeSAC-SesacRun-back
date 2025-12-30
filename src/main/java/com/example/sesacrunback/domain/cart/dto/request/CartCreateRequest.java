package com.example.sesacrunback.domain.cart.dto.request;

import com.example.sesacrunback.domain.cart.entity.CartItem;
import com.example.sesacrunback.domain.course.course.entity.Course;
import com.example.sesacrunback.domain.user.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartCreateRequest {
    @NotNull(message = "강의 ID는 필수입니다")
    private Long courseId;

    public static CartItem toEntity(User user, Course course) {
        return CartItem.builder()
                .user(user)
                .course(course)
                .build();
    }
}
