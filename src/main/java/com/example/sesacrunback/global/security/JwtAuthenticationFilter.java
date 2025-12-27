package com.example.sesacrunback.global.security;

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
                UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());

                // 6. SecurityContext에 Authentication 객체 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        }catch (Exception e){
            System.out.println("JWT 인증 실패: " + e.getMessage());
        }

        // 7. 다음 필터로 진행
        filterChain.doFilter(request , response);
    }
    
    // Authorization 헤더에서 토큰을 추출하는 메서드
    private String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
    


}
