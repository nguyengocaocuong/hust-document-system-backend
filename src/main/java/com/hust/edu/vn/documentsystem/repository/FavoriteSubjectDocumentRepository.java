package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.FavoriteSubjectDocument;
import com.hust.edu.vn.documentsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteSubjectDocumentRepository extends JpaRepository<FavoriteSubjectDocument, Long> {
    FavoriteSubjectDocument findByUser(User user);
}