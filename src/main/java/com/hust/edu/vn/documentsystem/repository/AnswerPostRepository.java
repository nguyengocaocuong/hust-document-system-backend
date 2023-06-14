package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.AnswerPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerPostRepository extends JpaRepository<AnswerPost, Long> {
    AnswerPost findByIdAndIsHidden(Long id, boolean b);

    @Query(value = "SELECT ap FROM AnswerPost ap WHERE ap.post.id = :postId")
    List<AnswerPost> findAllAnswerForPost(Long postId);
}
