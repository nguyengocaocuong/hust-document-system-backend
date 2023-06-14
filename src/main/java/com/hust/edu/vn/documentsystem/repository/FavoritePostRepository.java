package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.FavoritePost;
import com.hust.edu.vn.documentsystem.entity.Post;
import com.hust.edu.vn.documentsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoritePostRepository extends JpaRepository<FavoritePost, Long> {
    FavoritePost findByPostAndUser(Post post, User user);
}