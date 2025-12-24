package com.example.sesacrunback.domain.user.controller;

import com.example.sesacrunback.domain.user.dto.request.LoginReqDto;
import com.example.sesacrunback.domain.user.dto.request.SignUpReqDto;
import com.example.sesacrunback.domain.user.service.UserService;
import com.example.sesacrunback.global.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // 인증 / 토큰 구현전에 확인을 위해 String값으로 결과만 반환
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<String>> signUp(
        @RequestBody @Valid SignUpReqDto createReqDto
    ) {
        userService.signUp(createReqDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("회원가입 성공"));

    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(
        @RequestBody @Valid LoginReqDto loginReqDto
    ) {

        userService.login(loginReqDto);

        return ResponseEntity.ok().body(ApiResponse.success("로그인 성공"));
    }
}
