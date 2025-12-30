package com.example.sesacrunback.domain.course.section.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SectionUpdateReqDto {

    @NotBlank(message = "섹션 제목은 필수입니다.")
    private final String title;

    @NotNull(message = "섹션 순서는 필수입니다.")
    @Min(value = 0, message = "섹션 순서는 0 이상이어야 합니다.")
    private final Integer order;
}
