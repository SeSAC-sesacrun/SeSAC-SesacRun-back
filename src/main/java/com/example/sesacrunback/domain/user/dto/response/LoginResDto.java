package com.example.sesacrunback.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResDto {
    private String accessToken;
    private String refreshToken;
    private String tokenType;

    public static LoginResDto of(String accessToken , String refreshToken){
        return new LoginResDto(accessToken , refreshToken, "Bearer");
    }
}
