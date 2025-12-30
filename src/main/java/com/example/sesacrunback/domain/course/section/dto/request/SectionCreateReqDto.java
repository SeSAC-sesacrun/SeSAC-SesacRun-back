package com.example.sesacrunback.domain.course.section.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SectionCreateReqDto {

    @NotBlank(message = "섹션 제목은 필수입니다.")
    private final String title;
}
