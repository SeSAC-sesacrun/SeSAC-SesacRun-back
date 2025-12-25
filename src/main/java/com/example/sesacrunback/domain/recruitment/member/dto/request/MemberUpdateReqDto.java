package com.example.sesacrunback.domain.recruitment.member.dto.request;

import com.example.sesacrunback.domain.recruitment.member.entity.MemberStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberUpdateReqDto {

    @NotNull(message = "상태는 필수입니다.")
    private final MemberStatus status;

}
