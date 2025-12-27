package com.example.sesacrunback.global.security;

import com.example.sesacrunback.domain.user.entity.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    private final SecretKey key;
    // yml 파일에서 환경변수로 받아오는게 좋을지 하드코딩 해도 문제가 없을지 고민
    private final Long accessExpiration = 900000L;// 15분
    private final Long refreshExpiration = 1209600000L; //14일

    public JwtProvider(
        @Value("${jwt.secret}") String key) {
        // 환경변수로 String type으로 받은 jwt secret값을 SecretKey로 변환
        this.key = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }

    // 엑세스 토큰 생성
    public String createAccessToken(String email, UserRole role){
        return Jwts.builder()
                   .setSubject(email)
                   .claim("role", role)
                   .claim("type","access")
                   .setIssuedAt(new Date())
                   .setExpiration(new Date(System.currentTimeMillis() + accessExpiration))
                   .signWith(key)
                   .compact();
    }

    // 리프레시 토큰 생성
    public String createRefreshToken(String email){
        return Jwts.builder()
                   .setSubject(email)
                   .claim("type","refresh")
                   .setIssuedAt(new Date())
                   .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration))
                   .signWith(key)
                   .compact();
    }

    // 토큰에서 사용자 정보 추출
    public String getEmailFromToken(String token){
        return getClaims(token).getSubject();
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token){
        try{
            getClaims(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    // 토큰 만료 여부 확인
    public boolean isTokenExpired(String token){
        return getClaims(token).getExpiration().before(new Date());
    }


    // Claims 추출
    private Claims getClaims(String token){
        return Jwts.parserBuilder()
                   .setSigningKey(key)
                   .build()
                   .parseClaimsJws(token)
                   .getBody();
    }

}
