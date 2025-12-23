package com.example.sesacrunback.domain.recruitment.post.repository;

import com.example.sesacrunback.domain.recruitment.post.entity.RecruitmentPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitmentPostRepository extends JpaRepository<RecruitmentPost, Long> {

}
