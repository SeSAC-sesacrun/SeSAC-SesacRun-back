package com.example.sesacrunback.domain.user.controller;

import com.example.sesacrunback.domain.user.dto.response.UserResDto;
import com.example.sesacrunback.domain.user.service.UserService;
import com.example.sesacrunback.global.common.dto.ApiResponse;
import com.example.sesacrunback.global.security.CustomUserDetails;
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



}
