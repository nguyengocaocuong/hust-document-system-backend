package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.CommentReviewSubject;
import com.hust.edu.vn.documentsystem.entity.CommentReviewTeacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface CommentReviewTeacherRepository extends JpaRepository<CommentReviewTeacher, Long> {
    @Query("""
            SELECT crt
            FROM CommentReviewTeacher crt
            WHERE crt.reviewTeacher.id = :reviewTeacherId
            AND crt.isHidden = :b
            AND crt.parentComment.id = null
            AND crt.score > 0
            AND crt.isHidden = false
            """)
    List<CommentReviewTeacher> findAllByIdAndHidden(Long reviewTeacherId, boolean b);

    @Query(value = "SELECT crt FROM CommentReviewTeacher  crt WHERE crt.id = :commentId AND crt.owner.email = :email")
    CommentReviewTeacher findByIdAndUserEmail(Long commentId, String email);

    @Query(value = "SELECT crt FROM CommentReviewTeacher crt WHERE crt.id = :commentId AND crt.reviewTeacher.id = :reviewTeacherId AND crt.owner.email = :email")
    CommentReviewTeacher findByIdAndReviewTeacherIdAndOwnerEmail(Long commentId, Long reviewTeacherId, String email);


    @Query(value = "SELECT crt FROM CommentReviewTeacher crt WHERE crt.id = :commentId AND crt.reviewTeacher.id = :reviewTeacherId AND crt.reviewTeacher.owner.email = :email AND crt.isHidden = :isHidden")
    CommentReviewTeacher findByIdAndReviewTeacherIdAndHiddenAndReviewTeacherOwnerEmail(Long commentId, Long reviewTeacherId, boolean isHidden, String email);

    @Query(value = """
            SELECT c FROM CommentReviewTeacher c
            WHERE c.score < 0
            """)
    List<CommentReviewTeacher> findAllBabComments();

    CommentReviewTeacher findByIdAndIsHidden(Long commentId, boolean b);
}