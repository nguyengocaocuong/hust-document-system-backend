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
    CommentReviewSubject findByIdAndIsHidden(Long id, boolean hidden);

    @Query(value = "SELECT c FROM CommentReviewSubject  c WHERE c.reviewSubject.id = :reviewSubjectId")
    List<CommentReviewSubject> findAllByReviewSubjectId(Long reviewSubjectId);
}
