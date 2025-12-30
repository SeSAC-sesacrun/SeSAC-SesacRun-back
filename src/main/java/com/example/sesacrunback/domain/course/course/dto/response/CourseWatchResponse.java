package com.example.sesacrunback.domain.course.course.dto.response;

import com.example.sesacrunback.domain.course.course.entity.Course;
import com.example.sesacrunback.domain.course.lecture.entity.Lecture;
import com.example.sesacrunback.domain.course.section.entity.Section;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CourseWatchResponse {

    private final Long courseId;
    private final String title;
    private final List<SectionResponse> sections;

    private CourseWatchResponse(Long courseId, String title, List<SectionResponse> sections) {
        this.courseId = courseId;
        this.title = title;
        this.sections = sections;
    }

    public static CourseWatchResponse from(Course course) {
        return new CourseWatchResponse(
                course.getId(),
                course.getTitle(),
                course.getSections().stream()
                        .map(SectionResponse::from)
                        .collect(Collectors.toList())
        );
    }

    @Getter
    private static class SectionResponse {
        private final Long id;
        private final String title;
        private final List<LectureResponse> lectures;

        private SectionResponse(Long id, String title, List<LectureResponse> lectures) {
            this.id = id;
            this.title = title;
            this.lectures = lectures;
        }

        private static SectionResponse from(Section section) {
            return new SectionResponse(
                    section.getId(),
                    section.getTitle(),
                    section.getLectures().stream()
                            .map(LectureResponse::from)
                            .collect(Collectors.toList())
            );
        }
    }

    @Getter
    private static class LectureResponse {
        private final Long id;
        private final String title;
        private final String videoUrl;
        private final Integer duration;
        private final Integer order;

        private LectureResponse(Long id, String title, String videoUrl,
                                Integer duration, Integer order) {
            this.id = id;
            this.title = title;
            this.videoUrl = videoUrl;
            this.duration = duration;
            this.order = order;
        }

        private static LectureResponse from(Lecture lecture) {
            return new LectureResponse(
                    lecture.getId(),
                    lecture.getTitle(),
                    lecture.getVideoUrl(),
                    lecture.getDuration(),
                    lecture.getOrder()
            );
        }
    }
}
