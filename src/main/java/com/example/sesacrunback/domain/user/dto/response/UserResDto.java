package com.example.sesacrunback.domain.user.dto.response;

import com.example.sesacrunback.domain.user.entity.User;
import com.example.sesacrunback.domain.user.entity.UserRole;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserResDto {

    private Long id;
    private String email;
    private String name;
    private UserRole role;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;


    public static UserResDto from(User user) {
        return UserResDto.builder()
                   .id(user.getId())
                   .email(user.getEmail())
                   .name(user.getName())
                   .createdTime(user.getCreatedAt())
                   .updatedTime(user.getUpdatedAt())
                   .role(user.getRole())
                   .build();
    }
}
