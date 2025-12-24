package com.example.sesacrunback.domain.cart.entity;

import com.example.sesacrunback.domain.course.course.entity.Course;
import com.example.sesacrunback.domain.user.entity.User;
import com.example.sesacrunback.global.common.entity.BaseCreateEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "cart_items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItem extends BaseCreateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK


    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user; // 장바구니 주인

    @ManyToOne
    @JoinColumn(name = "course_id",nullable = false)
    private Course course; // 장바구니에 담은 강의

    @Builder
    public CartItem(User user, Course course) {
        this.user = user;
        this.course = course;
    }
}
