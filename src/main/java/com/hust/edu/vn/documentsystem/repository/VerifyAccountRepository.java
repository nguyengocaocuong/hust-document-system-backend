package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.User;
import com.hust.edu.vn.documentsystem.entity.VerifyAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerifyAccountRepository extends JpaRepository<VerifyAccount, Long> {
    VerifyAccount findByToken(String token);

    VerifyAccount findByUser(User user);
}