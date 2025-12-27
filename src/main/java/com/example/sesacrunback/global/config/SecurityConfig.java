package com.example.sesacrunback.global.config;

import com.example.sesacrunback.global.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 엔드포인트 권한 설정
            .authorizeHttpRequests(auth -> auth
                        // 코드이해를 높이기 위해 .anyRequest().authenticated()으로 처리하지 않고 Method 단위로 명시
                        // 인증
                        .requestMatchers("/api/auth/**").permitAll()// 회원가입, 로그인 : 누구나

                        // 강의                       
                        .requestMatchers(HttpMethod.GET, "/api/v1/courses/my").hasRole("INSTRUCTOR") // 조회: 강사
                        .requestMatchers(HttpMethod.POST,"/api/v1/courses").hasRole("INSTRUCTOR") // 작성: 강사
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/courses/**").hasRole("INSTRUCTOR") // 삭제: 강사
                        .requestMatchers(HttpMethod.GET, "/api/v1/courses/**").permitAll() // 조회: 누구나
                        
                        // 커뮤니티
                        .requestMatchers(HttpMethod.GET, "/api/recruitments/posts/**").permitAll()  // 조회: 누구나
                        .requestMatchers(HttpMethod.POST, "/api/recruitments/posts").authenticated()  // 작성: 로그인 필요
                        .requestMatchers(HttpMethod.PUT, "/api/recruitments/posts/**").authenticated()  // 수정: 로그인 필요
                        .requestMatchers(HttpMethod.DELETE, "/api/recruitments/posts/**").authenticated()  // 삭제: 로그인 필요
                        
                        // 장바구니
                        .requestMatchers("/api/cart/**").authenticated() //장바구니 모든 요청 : 로그인 필요

                        .anyRequest().authenticated()
        
            )
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
            
            return http.build();
    }

    
     //CORS 설정 -> 프론트엔드와 백엔드가 다른 포트/도메인에서 실행될 때 필요 
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(
            "http://localhost:3000"
        ));

        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
