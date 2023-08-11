package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.FavoriteSubjectDocument;
import com.hust.edu.vn.documentsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FavoriteSubjectDocumentRepository extends JpaRepository<FavoriteSubjectDocument, Long> {

    @Query("""
SELECT f
FROM FavoriteSubjectDocument f
WHERE f.subjectDocument.id = :subjectDocumentId AND f.user.id = :userId
""")
    FavoriteSubjectDocument findByIdAndUserId(Long subjectDocumentId, Long userId);
}