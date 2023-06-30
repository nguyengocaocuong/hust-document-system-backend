package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.FavoriteAnswerPost;
import com.hust.edu.vn.documentsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface FavoriteAnswerPostRepository extends JpaRepository<FavoriteAnswerPost, Long> {
    @Query(value = "SELECT fap FROM FavoriteAnswerPost fap WHERE fap.answerPost.id = :answerId AND fap.user = :user")
    FavoriteAnswerPost findByIdAndUser(Long answerId, User user);

    @Query(value = "SELECT fap FROM FavoriteAnswerPost fap WHERE fap.answerPost.id = :answerId")
    List<FavoriteAnswerPost> findAllByAnswerId(Long answerId);
}