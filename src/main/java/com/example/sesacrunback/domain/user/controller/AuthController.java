package com.example.sesacrunback.domain.user.controller;

import com.example.sesacrunback.domain.user.dto.request.LoginReqDto;
import com.example.sesacrunback.domain.user.dto.request.SignUpReqDto;
import com.example.sesacrunback.domain.user.dto.response.LoginResDto;
import com.example.sesacrunback.domain.user.entity.UserRole;
import com.example.sesacrunback.domain.user.service.UserService;
import com.example.sesacrunback.global.common.dto.ApiResponse;
import com.example.sesacrunback.global.security.CustomUserDetails;
import com.example.sesacrunback.global.security.JwtProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
        headers.set("Refresh-Token", resDto.getRefreshToken());

        return ResponseEntity.ok().headers(headers).body(ApiResponse.success(resDto.getRole()));
    }
    
    // 인증 테스트용 엔드포인트
    // SecurityConfig에서 /api/auth/** 는 permitAll()이므로 접근은 가능하지만,
    // 토큰이 없으면 userDetails가 null로 들어오거나 익명 사용자(anonymousUser)가 됩니다.
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<String>> getMyInfo(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            return ResponseEntity.ok().body(ApiResponse.success("현재 로그인하지 않은 상태입니다."));
        }

        // 인증이 정상적으로 완료되었다면 CustomUserDetails에서 정보를 꺼낼 수 있습니다.
        String info = String.format("인증 성공! 이메일: %s, 권한: %s, ID: %d", 
                                    userDetails.getUsername(), 
                                    userDetails.getRole(), 
                                    userDetails.getId());
        
        return ResponseEntity.ok().body(ApiResponse.success(info));
    }
}
