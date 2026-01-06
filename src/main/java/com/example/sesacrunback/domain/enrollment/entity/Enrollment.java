package com.example.sesacrunback.domain.enrollment.entity;

import com.example.sesacrunback.domain.course.course.entity.Course;
import com.example.sesacrunback.domain.order.entity.Order;
import com.example.sesacrunback.domain.user.entity.User;
import com.example.sesacrunback.global.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "enrollments",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_user_course_status",
        columnNames = {"user_id", "course_id", "status"}
    ),
    indexes = {
        @Index(name = "idx_user_status", columnList = "user_id, status"),
        @Index(name = "idx_course_status", columnList = "course_id, status")
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Enrollment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order; // 어떤 결제로 생긴 수강권인가 (추적용)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnrollmentStatus status;

    public static Enrollment forOrder(User user, Course course, Order order) {
        return Enrollment.builder()
            .user(user)
            .course(course)
            .order(order)
            .status(EnrollmentStatus.ACTIVE)
            .build();
    }

    public void cancel() {
        this.status = EnrollmentStatus.CANCELED;
    }
}
