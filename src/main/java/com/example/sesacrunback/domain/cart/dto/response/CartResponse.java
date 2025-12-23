package com.example.sesacrunback.domain.cart.dto.response;

import com.example.sesacrunback.domain.cart.entity.CartItem;
import lombok.Builder;
import lombok.Getter;
import com.example.sesacrunback.domain.course.course.entity.Course;

@Getter
@Builder
public class CartResponse {
    private Long cartItemId;
    private Long courseId;
    private final String courseTitle;
    private final String instructorName;
    private final String thumbnail;
    private final Integer price;

    @Builder
    public CartResponse(Long cartItemId, Long courseId, String courseTitle, String instructorName, String thumbnail, Integer price) {
        this.cartItemId = cartItemId;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.instructorName = instructorName;
        this.thumbnail = thumbnail;
        this.price = price;
    }

    public static CartResponse from(CartItem cartItem) {
        Course course = cartItem.getCourse();
        return CartResponse.builder()
                .cartItemId(cartItem.getId())
                .courseId(course.getId())
                .courseTitle(course.getTitle())
                //.instructorName(course.getUser().getName()) // TODO: 강좌이름 변경
                .instructorName("테스트") //
                .thumbnail(course.getThumbnail()) // 
                .price(course.getPrice())
                .build();
    }
}
