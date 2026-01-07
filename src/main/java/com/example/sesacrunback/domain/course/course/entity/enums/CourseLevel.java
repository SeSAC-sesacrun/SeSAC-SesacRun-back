package com.example.sesacrunback.domain.course.course.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 강의 난이도
 */
public enum CourseLevel {
    BEGINNER("초급"),
    INTERMEDIATE("중급"),
    ADVANCED("고급");

    private final String korean;

    CourseLevel(String korean) {
        this.korean = korean;
    }

    public String getKorean() {
        return korean;
    }

    /**
     * JSON 요청 시 대소문자 무관하게 파싱
     */
    @JsonCreator
    public static CourseLevel from(String value) {
        if (value == null) {
            return null;
        }
        for (CourseLevel level : values()) {
            if (level.name().equalsIgnoreCase(value)) {
                return level;
            }
        }
        throw new IllegalArgumentException("유효하지 않은 레벨 값입니다: " + value);
    }

    /**
     * JSON 응답 시 Enum 코드 그대로 반환
     */
    @JsonValue
    public String toValue() {
        return name();
    }
}
