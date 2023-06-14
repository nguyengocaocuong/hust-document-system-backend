package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.CommentReviewTeacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentReviewTeacherRepository extends JpaRepository<CommentReviewTeacher, Long> {
    @Query("SELECT crt FROM CommentReviewTeacher crt WHERE crt.reviewTeacher.id = :reviewTeacherId AND crt.isHidden = :b")
    List<CommentReviewTeacher> findAllByIdAndHidden(Long reviewTeacherId, boolean b);

    @Query(value = "SELECT crt FROM CommentReviewTeacher  crt WHERE crt.id = :commentId AND crt.owner.email = :email")
    CommentReviewTeacher findByIdAndUserEmail(Long commentId, String email);
}