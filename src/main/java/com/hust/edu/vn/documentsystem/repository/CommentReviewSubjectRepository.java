package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.CommentReviewSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface CommentReviewSubjectRepository extends JpaRepository<CommentReviewSubject, Long> {
    @Query(value = """
            SELECT c 
            FROM CommentReviewSubject  c
            WHERE c.reviewSubject.id = :reviewSubjectId
            AND c.parentComment.id = null
            AND c.score > 0
            AND c.isHidden = false
            """
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

    CommentReviewSubject findByIdAndIsHidden(Long commentId, boolean b);

    @Query(value = """
            SELECT c FROM CommentReviewSubject c
            WHERE c.score < 0 AND c.isHidden = false
            """)
    List<CommentReviewSubject> findAllBadComments();
}
