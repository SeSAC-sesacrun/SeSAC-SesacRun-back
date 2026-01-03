package com.example.sesacrunback.domain.user.dto.response;

import com.example.sesacrunback.domain.course.course.entity.Course;
import com.example.sesacrunback.domain.order.entity.Order;
import com.example.sesacrunback.domain.orderItem.entity.OrderItem;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class MyCourseResDto {

    private Long id;
    private Long courseId;
    private String courseTitle;
    private String thumbnail;
    private int price;
    private LocalDateTime purchasedAt;

    public static MyCourseResDto from(OrderItem orderItem){
        return MyCourseResDto.builder()
                   .id(orderItem.getId())
                   .courseId(orderItem.getCourse().getId())
                   .courseTitle(orderItem.getCourse().getTitle())
                   .thumbnail(orderItem.getCourse().getThumbnail())
                   .price(orderItem.getCourse().getPrice())
                   .build();

    }

}
