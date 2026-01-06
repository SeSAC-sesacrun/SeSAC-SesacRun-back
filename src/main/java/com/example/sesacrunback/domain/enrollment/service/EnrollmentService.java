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
     * - Enrollment 생성과 studentCount 증가는 같은 트랜잭션 내에서 원자적으로 처리됨
     * - 트랜잭션 롤백 시 Enrollment 생성과 studentCount 증가가 함께 취소됨
     * - 향후 비동기/이벤트 기반으로 변경 시 분리 고려 필요
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
                // ACTIVE Enrollment 생성 시 studentCount 증가
                course.increaseStudentCount();
            }

            processedCourseIds.add(course.getId());
        }
    }

    /**
     * 환불 시 Enrollment 취소
     * - ACTIVE 상태만 찾아 처리하므로 멱등성 보장
     * - 중복 호출 시에도 안전 (예외 발생 없음)
     * - Enrollment 취소와 studentCount 감소는 같은 트랜잭션 내에서 원자적으로 처리됨
     */
    @Transactional
    public void cancelForOrder(Order order, User user) {
        Set<Long> processedCourseIds = new HashSet<>();

        for (OrderItem orderItem : order.getOrderItems()) {
            Course course = orderItem.getCourse();

            // 동일 강의 중복 처리 방지
            if (processedCourseIds.contains(course.getId())) {
                continue;
            }

            enrollmentRepository
                    .findByUserIdAndCourseIdAndStatus(
                            user.getId(),
                            course.getId(),
                            EnrollmentStatus.ACTIVE
                    )
                    .ifPresent(enrollment -> {
                        enrollment.cancel();  // ACTIVE → CANCELED
                        course.decreaseStudentCount();
                    });

            processedCourseIds.add(course.getId());
        }
    }
}
