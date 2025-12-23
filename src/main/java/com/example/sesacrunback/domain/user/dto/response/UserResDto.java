package com.example.sesacrunback.domain.user.dto.response;

import com.example.sesacrunback.domain.user.dto.request.UserCreateReqDto;
import com.example.sesacrunback.domain.user.entity.User;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
public class UserResDto {
    private Long id;
    private String email;
    private String name;
    private String role;


    public UserResDto(Long id, String email, String name, String role) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.role = role;
    }

    public static UserResDto from(User user){
        return UserResDto.builder()
                   .id(user.getId())
                   .email(user.getEmail())
                   .name(user.getName())
                   .role(user.getRole().name())
                   .build();
    }
}
