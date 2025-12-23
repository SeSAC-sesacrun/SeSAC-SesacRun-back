package com.example.sesacrunback.domain.recruitment.post.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum RecruitmentCategory {
    STUDY,    // 스터디
    PROJECT;   // 팀 프로젝트

    // JSON 값을 받을 때 해당 메서드를 거침
    @JsonCreator
    public static RecruitmentCategory from(String value) {
        // 대소문자 관계 없이 일치하는 값 반환
        return RecruitmentCategory.valueOf(value.toUpperCase());
    }
}
