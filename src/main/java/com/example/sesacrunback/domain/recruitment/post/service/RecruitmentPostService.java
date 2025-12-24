package com.example.sesacrunback.domain.recruitment.post.service;

import com.example.sesacrunback.domain.recruitment.post.dto.request.RecruitmentPostCreateReqDto;
import com.example.sesacrunback.domain.recruitment.post.dto.request.RecruitmentPostUpdateReqDto;
import com.example.sesacrunback.domain.recruitment.post.dto.response.PostDetailResDto;
import com.example.sesacrunback.domain.recruitment.post.entity.RecruitmentCategory;
import com.example.sesacrunback.domain.recruitment.post.entity.RecruitmentPost;
import com.example.sesacrunback.domain.recruitment.post.entity.RecruitmentStatus;
import com.example.sesacrunback.domain.recruitment.post.repository.RecruitmentPostRepository;
import com.example.sesacrunback.domain.user.entity.User;
import com.example.sesacrunback.domain.user.repository.UserRepository;
import com.example.sesacrunback.global.exception.CustomException;
import com.example.sesacrunback.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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

        RecruitmentPost post = recruitmentPostRepository.save(reqDto.toEntity(user));
        return post.getId();
    }

    @Transactional
    public PostDetailResDto viewPost(Long postId) {
        RecruitmentPost post = getPostById(postId);
        // 조회 수 증가
        post.increaseViewCount();

        return PostDetailResDto.from(post);
    }

    public Slice<PostDetailResDto> viewPosts(RecruitmentCategory category, RecruitmentStatus status,
        Pageable pageable) {
        Slice<RecruitmentPost> posts = recruitmentPostRepository.findPosts(category, status,
            pageable);
        return posts.map(PostDetailResDto::from);
    }

    @Transactional
    public Long updatePost(Long postId, RecruitmentPostUpdateReqDto reqDto, Long userId) {
        RecruitmentPost post = getPostById(postId);

        validatePostOwner(post, userId);

        post.updatePost(reqDto.getCategory(), reqDto.getStatus(), reqDto.getTitle(),
            reqDto.getContent(), reqDto.getTotalMembers());

        return post.getId();
    }

    @Transactional
    public void deletePost(Long postId, Long userId) {
        RecruitmentPost post = getPostById(postId);

        validatePostOwner(post, userId);
        recruitmentPostRepository.delete(post);
    }

    private void validatePostOwner(RecruitmentPost post, Long userId) {
        if (!post.isPostOwner(userId)) {
            throw new CustomException(ErrorCode.POST_NOT_OWNER);
        }
    }

    private RecruitmentPost getPostById(Long postId) {
        return recruitmentPostRepository.findById(postId)
            .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }
}
