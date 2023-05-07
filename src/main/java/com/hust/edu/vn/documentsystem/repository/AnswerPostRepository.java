package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.AnswerPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerPostRepository extends JpaRepository<AnswerPost, Long> {
    AnswerPost findByIdAndIsHidden(Long id, boolean b);
}
