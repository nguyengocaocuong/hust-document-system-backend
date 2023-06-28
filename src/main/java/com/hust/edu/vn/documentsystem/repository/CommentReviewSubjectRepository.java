package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.CommentReviewSubject;
import com.hust.edu.vn.documentsystem.entity.ReviewSubject;
import com.hust.edu.vn.documentsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentReviewSubjectRepository extends JpaRepository<CommentReviewSubject, Long> {
    @Query(value = "" +
            "SELECT c " +
            "FROM CommentReviewSubject  c " +
            "WHERE c.reviewSubject.id = :reviewSubjectId " +
            "AND c.parentComment.id = null"
    )
    List<CommentReviewSubject> findAllByReviewSubjectId(Long reviewSubjectId);

    @Query(value = "" +
            "SELECT c " +
            "FROM CommentReviewSubject c " +
            "WHERE c.reviewSubject.id = :reviewSubjectId " +
            "AND c.id = :commentId " +
            "AND c.owner.email = :email"
    )
    CommentReviewSubject findByIdAndReviewSubjectIdAndHiddenAndOwnerReviewSubjectEmail(Long commentId, Long reviewSubjectId, String email);

    @Query(value = "" +
            "SELECT c " +
            "FROM CommentReviewSubject c " +
            "WHERE c.reviewSubject.id = :reviewSubjectId " +
            "AND c.id = :commentId " +
            "AND c.reviewSubject.owner.email = :email"
    )
    CommentReviewSubject findByIdAndReviewSubjectIdAndOwnerEmail(Long commentId, Long reviewSubjectId, String email);
}
