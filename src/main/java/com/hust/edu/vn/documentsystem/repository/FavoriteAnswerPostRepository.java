package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.FavoriteAnswerPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteAnswerPostRepository extends JpaRepository<FavoriteAnswerPost, Long> {
}