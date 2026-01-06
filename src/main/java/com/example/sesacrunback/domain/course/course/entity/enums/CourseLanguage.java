package com.example.sesacrunback.domain.course.course.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 강의 언어
 */
public enum CourseLanguage {
    KOREAN("한국어"),
    ENGLISH("영어");

    private final String korean;

    CourseLanguage(String korean) {
        this.korean = korean;
    }

    public String getKorean() {
        return korean;
    }

    /**
     * JSON 요청 시 대소문자 무관하게 파싱
     */
    @JsonCreator
    public static CourseLanguage from(String value) {
        return CourseLanguage.valueOf(value.toUpperCase());
    }

    /**
     * JSON 응답 시 Enum 코드 그대로 반환
     */
    @JsonValue
    public String toValue() {
        return name();
    }
}
