package com.example.sesacrunback.domain.course.lecture.dto.response;

import com.example.sesacrunback.domain.course.lecture.entity.Lecture;
import lombok.Getter;

@Getter
public class LectureResponse {
    private final Long id;
    private final String title;
    private final Integer duration;
    private final Integer order;
    private final String videoUrl;
    private final Boolean isFree;

    private LectureResponse(Long id, String title, Integer duration, Integer order,
                            String videoUrl, Boolean isFree) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.order = order;
        this.videoUrl = videoUrl;
        this.isFree = isFree;
    }

    public static LectureResponse from(Lecture lecture) {
        return new LectureResponse(
                lecture.getId(),
                lecture.getTitle(),
                lecture.getDuration(),
                lecture.getOrder(),
                lecture.getVideoUrl(),
                lecture.getIsFree()
        );
    }
}
