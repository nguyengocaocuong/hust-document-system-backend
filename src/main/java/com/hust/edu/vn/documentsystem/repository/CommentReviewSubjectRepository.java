package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.CommentReviewSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentReviewSubjectRepository extends JpaRepository<CommentReviewSubject, Long> {
    CommentReviewSubject findByIdAndIsHidden(Long id, boolean hidden);
}
