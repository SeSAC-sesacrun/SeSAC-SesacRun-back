package com.example.sesacrunback.domain.cart.service;

import com.example.sesacrunback.domain.cart.dto.request.CartCreateRequest;
import com.example.sesacrunback.domain.cart.dto.response.CartResponse;
import com.example.sesacrunback.domain.cart.entity.CartItem;
import com.example.sesacrunback.domain.cart.repository.CartRepository;
import com.example.sesacrunback.domain.course.course.entity.Course;
import com.example.sesacrunback.domain.course.course.repository.CourseRepository;
import com.example.sesacrunback.domain.user.entity.User;
import com.example.sesacrunback.domain.user.repository.UserRepository;
import com.example.sesacrunback.global.exception.CustomException;
import com.example.sesacrunback.global.exception.ErrorCode;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
//    private final OrderItemRepository orderItemRepository;

    @Transactional
    public CartResponse create(Long userId, CartCreateRequest req) {
        User user = findUserById(userId);
       Course course = courseRepository.findById(req.getCourseId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        cartRepository.findByUserAndCourse(user, course).ifPresent(item -> {
            throw new CustomException(ErrorCode.CART_ITEM_ALREADY_EXISTS);
        });
        // TODo : orderItemRepository 생성 후
        //boolean alreadyPurchased = orderItemRepository.existsByOrderUserAndCourseIdAndOrderStatus(user, course.getId(), OrderState.ORDER);
        boolean isOrderExist = false;
        if (isOrderExist) {
            throw new CustomException(ErrorCode.COURSE_ALREADY_PURCHASED);
        }

        CartItem cartItem = CartItem.builder()
                .user(user)
                .course(course)
                .build();

        CartItem savedItem = cartRepository.save(cartItem);
        return CartResponse.from(savedItem);
    }

    public List<CartResponse> findAll(Long userId) {
        User user = findUserById(userId);
        List<CartItem> cartItems = cartRepository.findAllByUser(user);

        return cartItems.stream()
                .map(CartResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long itemId, Long userId) {
        User user = findUserById(userId);
        CartItem cartItem = cartRepository.findByIdAndUser(itemId, user)
                .orElseThrow(() -> new CustomException(ErrorCode.CART_ITEM_NOT_FOUND));

        cartRepository.delete(cartItem);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

}
