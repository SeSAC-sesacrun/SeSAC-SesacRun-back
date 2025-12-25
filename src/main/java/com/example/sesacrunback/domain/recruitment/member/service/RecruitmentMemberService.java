package com.example.sesacrunback.domain.recruitment.member.service;

import com.example.sesacrunback.domain.recruitment.member.dto.request.MemberUpdateReqDto;
import com.example.sesacrunback.domain.recruitment.member.entity.MemberStatus;
import com.example.sesacrunback.domain.recruitment.member.entity.RecruitmentMember;
import com.example.sesacrunback.domain.recruitment.member.repository.RecruitmentMemberRepository;
import com.example.sesacrunback.domain.recruitment.post.entity.RecruitmentPost;
import com.example.sesacrunback.domain.recruitment.post.repository.RecruitmentPostRepository;
import com.example.sesacrunback.domain.user.entity.User;
import com.example.sesacrunback.domain.user.repository.UserRepository;
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
    private final RecruitmentPostRepository recruitmentPostRepository;
    private final UserRepository userRepository;

    public void createRecruitment(RecruitmentPost post, User user) {

        // 이미 등록된 멤버인지
        if (recruitmentMemberRepository.existsByPostAndUser(post, user)) {
            throw new CustomException(ErrorCode.ALREADY_RECRUITED_MEMBER);
        }
        recruitmentMemberRepository.save(RecruitmentMember.organizer(post, user));
    }

    @Transactional
    public Long applyToRecruitment(Long postId, Long userId) {

        RecruitmentPost post = getPostById(postId);

        User user = getUserById(userId);

        // 이미 신청 여부 검증
        recruitmentMemberRepository.findByPostAndUser(post, user)
            .ifPresent(RecruitmentMember::validateCanApply);

        recruitmentMemberRepository.save(RecruitmentMember.participant(post, user));
        return post.getId();
    }

    @Transactional
    public void updateMemberStatus(Long postId, Long memberId, Long userId, MemberUpdateReqDto reqDto) {

        RecruitmentPost post = getPostById(postId);
        User user = getUserById(userId);

        // 현재 요청하는 사람이 해당 모임에 참여 중인지
        RecruitmentMember requester = recruitmentMemberRepository.findByPostAndUser(post, user)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_RECRUITMENT_MEMBER));

        // 모집장이 맞는지
        if (!requester.isOrganizer()) {
            throw new CustomException(ErrorCode.NOT_RECRUITMENT_ORGANIZER);
        }

        RecruitmentMember member = recruitmentMemberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.RECRUITED_MEMBER_NOT_FOUND));

        switch (reqDto.getStatus()) {
            case MemberStatus.APPROVED -> {
                post.increaseCurrentCount();
                member.approve();
            }

            case MemberStatus.REJECTED -> {
                member.reject();
            }

            default -> throw new CustomException(ErrorCode.INVALID_REQUEST);
        }

    }

    private RecruitmentPost getPostById(Long postId) {
        return recruitmentPostRepository.findById(postId)
            .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
