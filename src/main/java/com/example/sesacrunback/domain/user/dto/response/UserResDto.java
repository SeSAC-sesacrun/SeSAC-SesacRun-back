package com.example.sesacrunback.domain.user.dto.response;

import com.example.sesacrunback.domain.user.entity.User;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public class UserResDto {

    private Long id;
    private String email;
    private String name;
    private String role;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;


    public static UserResDto from(User user) {
        return UserResDto.builder()
                   .id(user.getId())
                   .email(user.getEmail())
                   .name(user.getName())
                   .createdTime(user.getCreatedAt())
                   .updatedTime(user.getUpdatedAt())
                   .role(user.getRole().name())
                   .build();
    }
}
