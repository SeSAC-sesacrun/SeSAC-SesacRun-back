package com.example.sesacrunback.domain.user.service;

import com.example.sesacrunback.domain.order.dto.response.OrderResponse;
import com.example.sesacrunback.domain.order.entity.Order;
import com.example.sesacrunback.domain.order.entity.OrderState;
import com.example.sesacrunback.domain.order.repository.OrderRepository;
import com.example.sesacrunback.domain.recruitment.member.entity.RecruitmentMember;
import com.example.sesacrunback.domain.recruitment.member.repository.RecruitmentMemberRepository;
import com.example.sesacrunback.domain.recruitment.post.entity.RecruitmentPost;
import com.example.sesacrunback.domain.recruitment.post.repository.RecruitmentPostRepository;
import com.example.sesacrunback.domain.user.dto.response.MyCourseResDto;
import com.example.sesacrunback.domain.user.dto.response.MyMeetingResDto;
import com.example.sesacrunback.domain.user.dto.response.MyPostResDto;
import com.example.sesacrunback.domain.user.dto.response.UserResDto;
import com.example.sesacrunback.domain.user.entity.User;
import com.example.sesacrunback.domain.user.repository.UserRepository;
import com.example.sesacrunback.global.exception.CustomException;
import com.example.sesacrunback.global.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final RecruitmentPostRepository recruitmentPostRepository;
    private final RecruitmentMemberRepository recruitmentMemberRepository;

    public UserResDto getMyProfile(Long id) {

        return UserResDto.from(userRepository.getReferenceById(id));
    }

    public List<OrderResponse> getMyPurchases(Long id) {


        List<Order> orders = orderRepository.findAllByUserId(id);

        return orders.stream()
                   .filter(order -> order.getStatus() == OrderState.COMPLETED)
                   .map(OrderResponse::from).toList();
    }

    public List<MyCourseResDto> getMyCourses(Long id) {


        List<Order> orders = orderRepository.findAllByUserId(id);

        return orders.stream()
            .filter(order -> order.getStatus() == OrderState.COMPLETED)
            .flatMap(order -> order.getOrderItems().stream())
            .map(MyCourseResDto::from)
            .toList();

    }

    // 내가 작성한 게시글 조회
    public List<MyPostResDto> getMyPosts(Long id) {

        List<RecruitmentPost> posts = recruitmentPostRepository.findAllByAuthorId(id);

        return posts.stream()
                   .map(MyPostResDto::from)
                   .toList();
    }

    // 내가 참여한 모임 조회
    public List<MyMeetingResDto> getMyMeetings(Long id) {

        List<RecruitmentMember> members = recruitmentMemberRepository.findAllByUserId(id);

        return members.stream()
                   .map(MyMeetingResDto::from)
                   .toList();
    }
}
