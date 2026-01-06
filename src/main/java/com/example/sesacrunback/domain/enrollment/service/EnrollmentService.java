package com.example.sesacrunback.domain.enrollment.service;

import com.example.sesacrunback.domain.course.course.entity.Course;
import com.example.sesacrunback.domain.enrollment.entity.Enrollment;
import com.example.sesacrunback.domain.enrollment.entity.EnrollmentStatus;
import com.example.sesacrunback.domain.enrollment.repository.EnrollmentRepository;
import com.example.sesacrunback.domain.order.entity.Order;
import com.example.sesacrunback.domain.orderItem.entity.OrderItem;
import com.example.sesacrunback.domain.user.entity.User;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    /**
     * Order 결제 완료 시 Enrollment 생성
     */
    @Transactional
    public void createForOrder(Order order, User user) {
        Set<Long> processedCourseIds = new HashSet<>();

        for (OrderItem orderItem : order.getOrderItems()) {
            Course course = orderItem.getCourse();

            // 동일 강의 중복 구매 방지
            if (processedCourseIds.contains(course.getId())) {
                continue;
            }

            // 이미 ACTIVE 상태인 Enrollment가 있는지 확인
            if (!enrollmentRepository.existsByUserIdAndCourseIdAndStatus(
                    user.getId(), course.getId(), EnrollmentStatus.ACTIVE)) {
                enrollmentRepository.save(Enrollment.forOrder(user, course, order));
            }

            processedCourseIds.add(course.getId());
        }
    }

    /**
     * 환불 시 Enrollment 취소
     */
    @Transactional
    public void cancelForOrder(Order order, User user) {
        for (OrderItem orderItem : order.getOrderItems()) {
            enrollmentRepository
                    .findByUserIdAndCourseIdAndStatus(
                            user.getId(),
                            orderItem.getCourse().getId(),
                            EnrollmentStatus.ACTIVE
                    )
                    .ifPresent(Enrollment::cancel);
        }
    }
}
