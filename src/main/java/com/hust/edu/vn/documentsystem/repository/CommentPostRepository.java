package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.CommentPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentPostRepository extends JpaRepository<CommentPost, Long> {
    CommentPost findByIdAndIsHidden(Long id, boolean b);

    @Query(value = "SELECT cp FROM CommentPost cp " +
            "WHERE cp.parentComment = null " +
            "OR cp.id = :postId " +
            "AND (cp.isHidden = false " +
            "OR cp.owner.email = :email)")
    List<CommentPost> findAllComment(Long postId, String email);

    @Query(value = "SELECT cp FROM CommentPost cp WHERE cp.id = :commentId AND cp.owner.email = :email")
    CommentPost findByIdAndUserEmail(Long commentId, String email);
}