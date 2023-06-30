package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.AnswerPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface AnswerPostRepository extends JpaRepository<AnswerPost, Long> {
    AnswerPost findByIdAndIsHidden(Long id, boolean b);

    @Query(value = "SELECT ap FROM AnswerPost ap WHERE ap.post.id = :postId")
    List<AnswerPost> findAllAnswerForPost(Long postId);

    @Query(value = "SELECT ap FROM AnswerPost ap WHERE ap.post.id = :postId AND ap.id = :answerId")
    AnswerPost findByIdAndPostId(Long answerId, Long postId);

    @Query(value = "SELECT ap FROM AnswerPost ap WHERE ap.id = :answerId AND ap.post.id = :postId AND ap.owner.email = :email")
    AnswerPost findByIdAndPostIdAndOnwerEmail(Long answerId, Long postId, String email);

}
