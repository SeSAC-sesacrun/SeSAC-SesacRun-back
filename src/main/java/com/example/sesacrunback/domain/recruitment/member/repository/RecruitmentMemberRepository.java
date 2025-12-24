package com.example.sesacrunback.domain.recruitment.member.repository;

import com.example.sesacrunback.domain.recruitment.member.entity.RecruitmentMember;
import com.example.sesacrunback.domain.recruitment.post.entity.RecruitmentPost;
import com.example.sesacrunback.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitmentMemberRepository extends JpaRepository <RecruitmentMember, Long> {

    boolean existsByPostAndUser(RecruitmentPost post, User user);
}
