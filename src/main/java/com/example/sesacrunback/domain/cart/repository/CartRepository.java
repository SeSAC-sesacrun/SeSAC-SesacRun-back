package com.example.sesacrunback.domain.cart.repository;

import com.example.sesacrunback.domain.cart.entity.CartItem;
import com.example.sesacrunback.domain.course.course.entity.Course;
import com.example.sesacrunback.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByUserAndCourse(User user, Course course);
    List<CartItem> findAllByUser(User user);
    Optional<CartItem> findByIdAndUser(Long id, User user);
}
