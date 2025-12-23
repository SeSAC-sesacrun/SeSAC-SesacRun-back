package com.example.sesacrunback.domain.recruitment.post.dto.request;

import com.example.sesacrunback.domain.recruitment.post.entity.RecruitmentCategory;
import com.example.sesacrunback.domain.recruitment.post.entity.RecruitmentPost;
import com.example.sesacrunback.domain.user.entity.User;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RecruitmentPostCreateReqDto {

    @NotNull
    private final RecruitmentCategory category;

    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 50, message = "제목은 50자 이내로 작성해주세요.")
    private final String title;

    @NotBlank(message = "내용은 필수입니다.")
    private final String content;

    @NotNull(message = "모집 인원은 필수입니다.")
    @Min(value = 1, message = "모집 인원은 최소 1명 이상이어야 합니다.")
    private final Integer totalMembers;


    public RecruitmentPost toEntity(RecruitmentPostCreateReqDto reqDto, User author) {
        return RecruitmentPost.of(reqDto.getCategory(), reqDto.getTitle(), reqDto.content,
            reqDto.totalMembers, author);
    }

}
