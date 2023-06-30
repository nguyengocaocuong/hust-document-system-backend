package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.FavoriteReviewSubject;
import com.hust.edu.vn.documentsystem.entity.ReviewSubject;
import com.hust.edu.vn.documentsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface FavoriteReviewSubjectRepository extends JpaRepository<FavoriteReviewSubject, Long> {
    FavoriteReviewSubject findByReviewSubjectAndUser(ReviewSubject reviewSubject, User user);

    @Query(value = "SELECT f FROM FavoriteReviewSubject f WHERE f.reviewSubject.id = :reviewSubjectId")
    List<FavoriteReviewSubject> findAllByReviewSubjectId(Long reviewSubjectId);
}