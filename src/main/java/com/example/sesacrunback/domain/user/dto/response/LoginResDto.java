package com.example.sesacrunback.domain.user.dto.response;

import com.example.sesacrunback.domain.user.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResDto {
    private String accessToken;
    private String refreshToken;
    private UserRole role;

    public static LoginResDto of(String accessToken , String refreshToken, UserRole role){
        return new LoginResDto(accessToken , refreshToken, role);
    }
}
