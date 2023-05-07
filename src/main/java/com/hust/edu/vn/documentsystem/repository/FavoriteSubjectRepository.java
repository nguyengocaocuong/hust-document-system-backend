package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.FavoriteSubject;
import com.hust.edu.vn.documentsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteSubjectRepository extends JpaRepository<FavoriteSubject, Long> {
    List<FavoriteSubject> findByUser(User user);
}