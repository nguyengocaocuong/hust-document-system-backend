package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.FavoriteReviewTeacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteReviewTeacherRepository extends JpaRepository<FavoriteReviewTeacher, Long> {
}