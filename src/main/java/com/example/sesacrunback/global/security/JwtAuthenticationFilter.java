package com.example.sesacrunback.global.security;

import com.example.sesacrunback.global.common.dto.ApiError;
import com.example.sesacrunback.global.common.dto.ApiResponse;
import com.example.sesacrunback.global.exception.CustomException;
import com.example.sesacrunback.global.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        try {
            // 1. 헤더에서 토큰 추출
            String token = resolveToken(request);

            // 2. 토큰 유효성 검사
            if (StringUtils.hasText(token) && jwtProvider.validateToken(token)) {
                // 3. 토큰에서 사용자 정보 추출
                String email = jwtProvider.getEmailFromToken(token);

                // 4. 사용자 정보로 UserDetails 객체 생성
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                // 5. Spring Security 전용 인증 객체 생성
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null,
                        userDetails.getAuthorities());

                // 6. SecurityContext에 Authentication 객체 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            // 7. 다음 필터로 진행
        filterChain.doFilter(request , response);

        }
        catch (CustomException e) {
             // 2. JWT 검증 중 발생한 CustomException(만료, 잘못된 서명 등)을 직접 응답
        sendErrorResponse(response, e.getErrorCode());
        }
        catch (Exception e){
             // 3. 그 외 예상치 못한 예외 처리
        logger.error("JWT 인증 실패: " + e.getMessage());
        sendErrorResponse(response, ErrorCode.INTERNAL_ERROR); // 혹은 적절한 권한 에러
        }

        
    }
    
    // Authorization 헤더에서 토큰을 추출하는 메서드
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 직접 JSON 응답을 만드는 헬퍼 메서드
    private void sendErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getStatus().value());
        response.setContentType("application/json;charset=UTF-8");

        ApiResponse<Void> apiResponse = ApiResponse.error(ApiError.of(errorCode));
        String json = objectMapper.writeValueAsString(apiResponse);

        response.getWriter().write(json);
    }
    


}
