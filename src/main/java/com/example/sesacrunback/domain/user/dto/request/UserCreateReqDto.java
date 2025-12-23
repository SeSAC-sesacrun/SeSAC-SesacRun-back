package com.example.sesacrunback.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserCreateReqDto {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    @Email
    private String name;

}
