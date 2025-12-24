package com.example.sesacrunback.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginReqDto {

    @NotBlank(message = "이메일은 필수로 입력해주세요")
    @Email
    private String email;

    @NotBlank(message = "비밀번호는 필수로 입력해주세요")
    private String password;
}
