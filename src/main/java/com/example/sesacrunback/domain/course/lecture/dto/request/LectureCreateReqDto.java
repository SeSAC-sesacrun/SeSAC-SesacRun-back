package com.example.sesacrunback.domain.course.lecture.dto.request;

import com.example.sesacrunback.domain.course.lecture.entity.Lecture;
import com.example.sesacrunback.domain.course.section.entity.Section;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LectureCreateReqDto {

    @NotBlank(message = "강의 제목은 필수입니다")
    @Size(max = 255)
    private final String title;

    @NotNull(message= "강의 순서는 필수입니다.")
    private final Integer order;

    @NotBlank(message = "영상 URL은 필수입니다.")
    @Size(max = 255)
    private final String videoUrl;

    private final Integer duration;

    private final Boolean isFree;

    public Lecture toEntity(Section section) {
        return Lecture.of(
                section,
                title,
                order,
                videoUrl,
                duration,
                isFree
        );
    }
}
