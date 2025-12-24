package com.example.sesacrunback.domain.course.section.dto.response;

import com.example.sesacrunback.domain.course.lecture.dto.response.LectureResponse;
import com.example.sesacrunback.domain.course.section.entity.Section;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class SectionResponse {
    private final Long id;
    private final String title;
    private final Integer order;
    private final List<LectureResponse> lectures;

    private SectionResponse(Long id, String title, Integer order, List<LectureResponse> lectures) {
        this.id = id;
        this.title = title;
        this.order = order;
        this.lectures = lectures;
    }

    public static SectionResponse from(Section section) {
        return new SectionResponse(
                section.getId(),
                section.getTitle(),
                section.getOrder(),
                section.getLectures().stream()
                        .map(LectureResponse::from)
                        .collect(Collectors.toList())
        );
    }
}
