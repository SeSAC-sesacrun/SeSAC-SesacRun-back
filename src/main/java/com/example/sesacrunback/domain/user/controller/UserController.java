package com.example.sesacrunback.domain.user.controller;

import com.example.sesacrunback.domain.order.dto.response.OrderResponse;
import com.example.sesacrunback.domain.user.dto.response.MyCourseResDto;
import com.example.sesacrunback.domain.user.dto.response.MyPostResDto;
import com.example.sesacrunback.domain.user.dto.response.UserResDto;
import com.example.sesacrunback.domain.user.service.UserService;
import com.example.sesacrunback.global.common.dto.ApiResponse;
import com.example.sesacrunback.global.security.CustomUserDetails;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResDto>> getMyProfile(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ){

        return ResponseEntity.ok(ApiResponse.success(userService.getMyProfile(userDetails.getId())));
    }


    // 내 결제 주문 부러오기 ( 하나의 주문 기준 , 해당 API )
    @GetMapping("/me/purchases")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getMyPurchases(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        return ResponseEntity.ok(ApiResponse.success(userService.getMyPurchases(userDetails.getId())));
    }



    // 결제한 모든 강의 조회
    @GetMapping("/me/courses")
    public ResponseEntity<ApiResponse<List<MyCourseResDto>>> getMyCourses(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        return ResponseEntity.ok(ApiResponse.success(userService.getMyCourses(userDetails.getId())));
    }


    // 내가 작성한 게시글
    @GetMapping("/me/posts")
    public ResponseEntity<ApiResponse<List<MyPostResDto>>> getMyPosts(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        return ResponseEntity.ok(ApiResponse.success(userService.getMyPosts(userDetails.getId())));
    }

}
