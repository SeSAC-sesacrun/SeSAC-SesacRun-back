package com.example.sesacrunback.domain.recruitment.member.service;

import com.example.sesacrunback.domain.recruitment.member.entity.RecruitmentMember;
import com.example.sesacrunback.domain.recruitment.member.repository.RecruitmentMemberRepository;
import com.example.sesacrunback.domain.recruitment.post.entity.RecruitmentPost;
import com.example.sesacrunback.domain.user.entity.User;
import com.example.sesacrunback.global.exception.CustomException;
import com.example.sesacrunback.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecruitmentMemberService {

    private final RecruitmentMemberRepository recruitmentMemberRepository;

    public void createRecruitment(RecruitmentPost post, User user) {
        
        // 이미 등록된 멤버인지
        if (recruitmentMemberRepository.existsByPostAndUser(post, user)) {
            throw new CustomException(ErrorCode.ALREADY_RECRUITED_MEMBER);
        }
        recruitmentMemberRepository.save(RecruitmentMember.of(post, user));
    }

}
