package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.SharePrivate;
import com.hust.edu.vn.documentsystem.entity.SubjectDocument;
import com.hust.edu.vn.documentsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SharePrivateRepository extends JpaRepository<SharePrivate, Long> {
    SharePrivate findBySubjectDocumentAndUser(SubjectDocument subjectDocument, User user);

    List<User> findByUser(User user);

    @Query(value = "SELECT s.user FROM SharePrivate s WHERE s.subjectDocument.id = :subjectDocumentId")
    List<User> findBySubjectDocumentId(Long subjectDocumentId);

    @Query(value = "SELECT sp FROM SharePrivate sp WHERE sp.user.email = :email")
    List<SharePrivate> findAllByUserEmail(String email);

    @Query(value = "SELECT sp FROM SharePrivate  sp WHERE sp.id = :sharedId AND sp.subjectDocument.id = :subjectDocumentId AND sp.user.email = :email")
    SharePrivate findByIdAndSubjectDocumentIdAndUserEmail(Long sharedId, Long subjectDocumentId, String email);
}