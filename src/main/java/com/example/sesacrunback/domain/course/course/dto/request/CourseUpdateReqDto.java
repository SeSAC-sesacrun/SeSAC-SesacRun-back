package com.example.sesacrunback.domain.course.course.dto.request;

import com.example.sesacrunback.domain.course.course.entity.enums.CourseLanguage;
import com.example.sesacrunback.domain.course.course.entity.enums.CourseLevel;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class CourseUpdateReqDto {

    @NotBlank(message = "강의 제목은 필수입니다")
    private final String title;

    @NotBlank(message = "간단 소개는 필수입니다")
    private final String description;

    @NotBlank(message = "상세 설명은 필수입니다")
    private final String detailedDescription;

    @NotBlank(message = "썸네일 URL은 필수입니다")
    private final String thumbnail;

    @NotBlank(message = "카테고리는 필수입니다")
    private final String category;

    @NotNull(message = "난이도는 필수입니다")
    private final CourseLevel level;

    @NotNull(message = "언어는 필수입니다")
    private final CourseLanguage language;

    @NotNull(message = "가격은 필수입니다")
    @Min(value = 0, message = "가격은 0원 이상이어야 합니다")
    private final Integer price;

    private final List<String> features;
}
