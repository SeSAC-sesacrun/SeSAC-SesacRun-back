package com.example.sesacrunback.domain.recruitment.member.dto.response;

import com.example.sesacrunback.domain.recruitment.member.entity.MemberStatus;
import com.example.sesacrunback.domain.recruitment.member.entity.RecruitmentMember;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RecruitmentMemberDto {

    private final Long id;
    private final Long userId;          // 신청자 userId
    private final String userName;      // 신청자 이름
    private final MemberStatus status;

    public static RecruitmentMemberDto from(RecruitmentMember member) {
        return new RecruitmentMemberDto(member.getId(), member.getUser().getId(),
            member.getUser().getName(), member.getStatus());
    }
}
