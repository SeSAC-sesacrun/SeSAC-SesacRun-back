package com.example.sesacrunback.domain.recruitment.post.dto.response;

import com.example.sesacrunback.domain.recruitment.post.entity.RecruitmentPost;
import com.example.sesacrunback.domain.recruitment.post.entity.RecruitmentStatus;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostDetailResDto { //Todo 작성자 Id 반환 필요

    private final Long postId;

    private final String title;

    private final String content;

    private final RecruitmentStatus status;

    private final String authorName;

    private final boolean isAuthor;

    private final int views;

    private final Integer currentMembers;

    private final Integer totalMembers;

    private final LocalDateTime createdAt;

    public static PostDetailResDto from(RecruitmentPost post, boolean isAuthor) {
        return new PostDetailResDto(post.getId(), post.getTitle(), post.getContent(),
            post.getStatus(), post.getAuthor().getName(), isAuthor, post.getViews(),
            post.getCurrentMembers(), post.getTotalMembers(), post.getCreatedAt());
    }


}
