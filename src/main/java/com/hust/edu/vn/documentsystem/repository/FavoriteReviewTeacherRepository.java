package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.FavoriteReviewTeacher;
import com.hust.edu.vn.documentsystem.entity.ReviewTeacher;
import com.hust.edu.vn.documentsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface FavoriteReviewTeacherRepository extends JpaRepository<FavoriteReviewTeacher, Long> {
    FavoriteReviewTeacher findByReviewTeacherAndUser(ReviewTeacher reviewTeacher, User user);

    @Query(value = "SELECT frt FROM FavoriteReviewTeacher frt WHERE frt.reviewTeacher.id = :reviewTeacherId")
    List<FavoriteReviewTeacher> findAllByReviewTeacherId(Long reviewTeacherId);
}