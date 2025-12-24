package com.example.sesacrunback.domain.user.dto.request;

import com.example.sesacrunback.domain.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SignUpReqDto {

    @NotBlank(message = "이메일은 필수입니다.")
    @Email
    private String email;

    @NotBlank(message = "비밀번호는 필수 입니다.")
    private String password;

    @NotBlank(message = "이름은 필수입니다,")
    @Size(min = 2, message = "이름의 최소길이는 2글자 입니다.")
    private String name;


    public static User toEntity(SignUpReqDto createReqDto){
        return User.builder()
                   .email(createReqDto.getEmail())
                   .password(createReqDto.getPassword())
                   .name(createReqDto.getName())
                   .build();
    }

}
