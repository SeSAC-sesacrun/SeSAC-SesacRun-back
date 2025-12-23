package com.example.sesacrunback.domain.user.dto.response;

import com.example.sesacrunback.domain.user.dto.request.UserCreateReqDto;
import com.example.sesacrunback.domain.user.entity.User;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
public class UserResDto {

    private Long id;
    private String email;
    private String name;
    private String role;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;


//    public UserResDto(Long id, String email, String name, String role, LocalDateTime createdTime,
//        LocalDateTime updatedTime) {
//
//        this.id = id;
//        this.email = email;
//        this.name = name;
//        this.role = role;
//        this.createdTime = createdTime;
//        this.updatedTime= updatedTime;
//    }

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
