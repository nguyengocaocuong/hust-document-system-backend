package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);
}