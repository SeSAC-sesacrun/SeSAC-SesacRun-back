package com.example.sesacrunback.domain.recruitment.post.service;

import com.example.sesacrunback.domain.recruitment.post.dto.request.RecruitmentPostCreateReqDto;
import com.example.sesacrunback.domain.recruitment.post.dto.response.PostDetailResDto;
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
public class RecruitmentPostService {

    private final UserRepository userRepository;
    private final RecruitmentPostRepository recruitmentPostRepository;

    @Transactional
    public Long createPost(RecruitmentPostCreateReqDto reqDto, Long userId) {

        // Todo userService 완료 후 변경 예정
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        RecruitmentPost post = recruitmentPostRepository.save(reqDto.toEntity(reqDto, user));
        return post.getId();
    }

    @Transactional
    public PostDetailResDto viewPost(Long postId) {
        RecruitmentPost post = recruitmentPostRepository.findById(postId)
            .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        // 조회 수 증가
        post.increaseViewCount();

        return PostDetailResDto.from(post);
    }
}
