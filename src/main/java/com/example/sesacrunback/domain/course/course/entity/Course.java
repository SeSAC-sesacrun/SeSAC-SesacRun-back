package com.example.sesacrunback.domain.course.course.entity;

import com.example.sesacrunback.domain.course.section.entity.Section;
import com.example.sesacrunback.domain.orderItem.entity.OrderItem;
import com.example.sesacrunback.domain.user.entity.User;
import com.example.sesacrunback.global.common.entity.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "courses")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

    @Column(nullable = false)
    private String title; // 강의 제목

    @Column(nullable = false)
    private String description; // 강의 설명 (짧은)

    private String detailedDescription; // 강의 상세 설명

    @Column(nullable = false)
    private String thumbnail; // 썸네일 이미지 URL


    @Column(nullable = false)
    private  String category; // 카테고리

    @Column(nullable = false)
    private Integer price; // 판매가

    private Integer originalPrice; // 정가 (할인 전 가격)

    @Column(nullable = false)
    private String level; // 난이도

    @Column(nullable = false)
    private String language; // 사용 언어

    @Column(nullable = false)
    private String duration; // 총 강의 시간

    @Column(nullable = false)
    private Integer studentCount; // 수강생 수

    @Column(nullable = false)
    private LocalDate lastUpdated; // 마지막 업데이트 일자

    private LocalDateTime deletedAt; // 삭제일시 (Soft Delete)

    @ManyToOne
    @JoinColumn(name = "instructor_id",nullable = false)
    private User instructor; // 강의자

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>(); // 강의에 포함된 섹션 목록

    @OneToMany(mappedBy = "course")
    private List<OrderItem> orderItems = new ArrayList<>(); // 이 강의를 포함하는 주문 항목 목록
}
