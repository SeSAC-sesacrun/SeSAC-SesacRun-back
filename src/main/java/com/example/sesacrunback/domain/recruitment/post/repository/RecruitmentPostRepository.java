package com.example.sesacrunback.domain.recruitment.post.repository;

import com.example.sesacrunback.domain.recruitment.post.entity.RecruitmentCategory;
import com.example.sesacrunback.domain.recruitment.post.entity.RecruitmentPost;
import com.example.sesacrunback.domain.recruitment.post.entity.RecruitmentStatus;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitmentPostRepository extends JpaRepository<RecruitmentPost, Long> {

    @Query("""
            select p from RecruitmentPost p
            where p.category = :category
            and (:status is null or p.status = :status)
            order by p.createdAt desc
           """)
    Slice<RecruitmentPost> findPosts(RecruitmentCategory category, RecruitmentStatus status, Pageable pageable);

    List<RecruitmentPost> findAllByAuthorId(Long id);
}
