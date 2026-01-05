package com.example.sesacrunback.domain.user.dto.response;

import com.example.sesacrunback.domain.recruitment.member.entity.MemberRole;
import com.example.sesacrunback.domain.recruitment.member.entity.RecruitmentMember;
import com.example.sesacrunback.domain.recruitment.post.entity.RecruitmentStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyMeetingResDto {
    private Long id;
    private Long postId;
    private String title;
    private RecruitmentStatus status;
    private MemberRole role;

    public static MyMeetingResDto from(RecruitmentMember member){
        return MyMeetingResDto.builder()
                   .id(member.getId())
                   .postId(member.getPost().getId())
                   .title(member.getPost().getTitle())
                   .status(member.getPost().getStatus())
                   .role(member.getRole())
                   .build();
    }
}
