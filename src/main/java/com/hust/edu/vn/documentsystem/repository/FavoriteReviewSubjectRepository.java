package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.FavoriteReviewSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteReviewSubjectRepository extends JpaRepository<FavoriteReviewSubject, Long> {
}