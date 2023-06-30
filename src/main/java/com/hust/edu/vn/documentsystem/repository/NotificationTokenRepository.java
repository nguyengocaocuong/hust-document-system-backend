package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.NotificationToken;
import com.hust.edu.vn.documentsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NotificationTokenRepository extends JpaRepository<NotificationToken, Long> {
    NotificationToken findByToken(String token);

    NotificationToken findByUser(User user);
}