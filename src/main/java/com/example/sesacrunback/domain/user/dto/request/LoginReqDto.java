package com.example.sesacrunback.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginReqDto {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}
