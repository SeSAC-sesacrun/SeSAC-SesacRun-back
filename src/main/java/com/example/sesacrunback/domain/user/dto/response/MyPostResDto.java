package com.example.sesacrunback.domain.user.dto.response;

import com.example.sesacrunback.domain.recruitment.post.entity.RecruitmentCategory;
import com.example.sesacrunback.domain.recruitment.post.entity.RecruitmentPost;
import com.example.sesacrunback.domain.recruitment.post.entity.RecruitmentStatus;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyPostResDto {
    private Long id;
    private RecruitmentCategory category;
    private RecruitmentStatus status;
    private String title;
    private Integer currentMembers;
    private Integer totalMembers;
    private Integer view;
    private LocalDateTime createdAt;

    public static MyPostResDto from(RecruitmentPost post){
        return MyPostResDto.builder()
                   .id(post.getId())
                   .category(post.getCategory())
                   .status(post.getStatus())
                   .title(post.getTitle())
                   .currentMembers(post.getCurrentMembers())
                   .totalMembers(post.getTotalMembers())
                   .view(post.getViews())
                   .createdAt(post.getCreatedAt())
                   .build();
    }
}
