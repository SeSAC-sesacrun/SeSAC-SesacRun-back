package com.example.sesacrunback.domain.recruitment.post.dto.response;

import com.example.sesacrunback.domain.recruitment.post.entity.RecruitmentPost;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostDetailResDto {

    private final Long postId;

    private final String title;

    private final String content;

    private final String authorName;

    private final Integer currentMembers;

    private final Integer totalMembers;

    private final LocalDateTime createdAt;

    public static PostDetailResDto from(RecruitmentPost post) {
        return new PostDetailResDto(post.getId(), post.getTitle(), post.getContent(),
            post.getAuthor().getName(), post.getCurrentMembers(), post.getTotalMembers(),
            post.getCreatedAt());
    }


}
