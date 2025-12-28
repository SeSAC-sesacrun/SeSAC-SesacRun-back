package com.example.sesacrunback.domain.course.lecture.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LectureUpdateReqDto {

    @NotBlank(message = "강의 제목은 필수입니다.")
    @Size(max = 255)
    private final String title;

    @NotNull(message = "강의 순서는 필수입니다.")
    @Min(value = 0, message = "강의 순서는 0 이상이어야 합니다.")
    private final Integer order;

    @NotBlank(message = "영상 URL은 필수입니다.")
    @Size(max = 500)
    private final String videoUrl;

    @Min(value = 0, message = "재생 시간은 0 이상이어야 합니다.")
    private final Integer duration;

    private final Boolean isFree;
}
