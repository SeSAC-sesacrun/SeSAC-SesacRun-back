package com.example.sesacrunback.domain.user.controller;

import com.example.sesacrunback.domain.user.dto.request.LoginReqDto;
import com.example.sesacrunback.domain.user.dto.request.SignUpReqDto;
import com.example.sesacrunback.domain.user.dto.response.LoginResDto;
import com.example.sesacrunback.domain.user.entity.UserRole;
import com.example.sesacrunback.domain.user.service.UserService;
import com.example.sesacrunback.global.common.dto.ApiResponse;
import com.example.sesacrunback.global.security.JwtProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import  org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // 인증 / 토큰 구현전에 확인을 위해 String값으로 결과만 반환
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<String>> signUp(
        @RequestBody @Valid SignUpReqDto signUpReqDto
    ) {
        userService.signUp(signUpReqDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("회원가입 성공"));

    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserRole>> login(
        @RequestBody @Valid LoginReqDto loginReqDto
    ) {
        LoginResDto resDto = userService.login(loginReqDto);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + resDto.getAccessToken());
        headers.set("Refresh-Token",resDto.getRefreshToken());


        return ResponseEntity.ok().headers(headers).body(ApiResponse.success(resDto.getRole()));
    }
}
