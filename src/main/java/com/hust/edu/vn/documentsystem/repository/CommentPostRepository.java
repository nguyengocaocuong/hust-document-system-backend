package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.CommentPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentPostRepository extends JpaRepository<CommentPost, Long> {

    @Query(value = "SELECT cp FROM CommentPost cp WHERE cp.post.id = :postId AND cp.post.owner.email = :email AND cp.id = :commentId AND cp.isHidden = :b")
    CommentPost findByIdAndPostIdAndIsHidden(Long commentId, Long postId,String email, boolean b);

    @Query(value = "SELECT cp FROM CommentPost cp " +
            "WHERE cp.parentComment = null " +
            "AND cp.post.id = :postId " +
            "AND cp.isHidden = false ")
    List<CommentPost> findAllComment(Long postId);

    @Query(value = "SELECT cp FROM CommentPost cp WHERE cp.id = :commentId AND cp.post.id = :postId AND cp.owner.email = :email")
    CommentPost findByIdAndPostIdAndUserEmail(Long commentId, Long postId,String email);

    @Query(value = "SELECT cp FROM CommentPost cp WHERE cp.post.id = :postId AND cp.id = :commentId")
    CommentPost findByIdAndPostId(Long commentId, Long postId);
}