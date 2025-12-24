package com.example.sesacrunback.domain.user.entity;

import com.example.sesacrunback.domain.cart.entity.CartItem;
import com.example.sesacrunback.domain.chat.message.entity.ChatMessage;
import com.example.sesacrunback.domain.course.course.entity.Course;
import com.example.sesacrunback.domain.order.entity.Order;
import com.example.sesacrunback.domain.recruitment.member.entity.RecruitmentMember;
import com.example.sesacrunback.domain.recruitment.post.entity.RecruitmentPost;
import com.example.sesacrunback.domain.user.dto.request.SignUpReqDto;
import com.example.sesacrunback.global.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

    @Column(nullable = false, unique = true)
    @Email
    private String email; // 이메일 (로그인 ID)

    @Column(nullable = false)
    private String password; // 비밀번호

    @Column(nullable = false)
    private String name; // 이름

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.USER; // 역할 (일반 사용자, 강의자)

    private LocalDateTime deletedAt; // 삭제일시 (Soft Delete)

    @OneToMany(mappedBy = "instructor")
    private List<Course> createdCourses = new ArrayList<>(); // 이 사용자가 생성한 강의 목록

    @OneToMany(mappedBy = "author")
    private List<RecruitmentPost> createdRecruitmentPosts = new ArrayList<>(); // 이 사용자가 작성한 모집글 목록

    @OneToMany(mappedBy = "user")
    private List<Order> orders = new ArrayList<>(); // 이 사용자의 주문 목록

    @OneToMany(mappedBy = "sender")
    private List<ChatMessage> sentChatMessages = new ArrayList<>(); // 이 사용자가 보낸 채팅 메시지 목록

    @OneToMany(mappedBy = "user")
    private List<RecruitmentMember> recruitmentMemberships = new ArrayList<>(); // 이 사용자가 참여한 모집 그룹 목록

    @OneToMany(mappedBy = "user")
    private List<CartItem> cartItems = new ArrayList<>(); // 이 사용자의 장바구니 항목 목록

    @Builder
    private User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }



}
